package org.example.model.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.discount.CouponClient;
import org.example.discount.CouponDTO;
import org.example.notification.LoggerClient;
import org.example.dto.ProductResponse;
import org.example.notification.LoggerDTO;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.example.dto.ProductRequest;
import org.example.exception.NotFoundProduct;
import org.example.model.entity.Coupon;
import org.example.model.entity.Product;
import org.example.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;

    @Qualifier("org.example.notification.LoggerClient")
    private final LoggerClient loggerClient;

    @Qualifier("org.example.discount.CouponClient")
    private final CouponClient couponClient;
    private final RestClient.Builder restClient;
    private final WebClient.Builder webClient;

    @Override
    @CircuitBreaker(name = "myClient", fallbackMethod = "createProductFallBack")
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapper.map(productRequest, Product.class);
//        CouponDTO couponDTO = restTemplate.getForObject("http://DISCOUNT/api/v1/coupon/find/{code}", CouponDTO.class, product.getCode());
//        CouponDTO couponDTO =  Objects.requireNonNull(webClient.build().get()
//                .uri("http://DISCOUNT/api/v1/coupon/find/{code}", product.getCode())
//                .retrieve().toEntity(CouponDTO.class).block()).getBody();
//        CouponDTO couponDTO = restClient.build().get().uri("http://DISCOUNT/api/v1/coupon/find/{code}", product.getCode())
//                .retrieve().toEntity(CouponDTO.class).getBody();

        CouponDTO couponDTO = couponClient.findByCode(product.getCode());

        if (couponDTO.getDiscount() != null) {
            BigDecimal subtract = new BigDecimal("100").subtract(couponDTO.getDiscount());
            product.setPrice(subtract.multiply(product.getPrice()).divide(new BigDecimal("100")));
        }

        ProductResponse productResponse = mapper.map(productRepository.save(product), ProductResponse.class);

        LoggerDTO logger = new LoggerDTO();
        logger.setSender("product");
        logger.setMessage(productResponse.toString());
        logger.setLocalDateTime(LocalDateTime.now());
        createLog(logger);
        return productResponse;
    }
    public ProductResponse createProductFallBack(Throwable throwable) {
        return new ProductResponse();
    }

    @Override
    public ProductResponse findById(Long id) throws NotFoundProduct {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundProduct("not found product");
        }
        return mapper.map(product.get(), ProductResponse.class);
    }

    @Override
    public void createLog(LoggerDTO logger) {
        loggerClient.createLog(logger);
    }
}
