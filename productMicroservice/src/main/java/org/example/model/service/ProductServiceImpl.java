package org.example.model.service;

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
    private final LoggerClient loggerClient;
    private final CouponClient couponClient;
    private final RestClient restClient;
    private final WebClient webClient;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapper.map(productRequest, Product.class);
//        Product product = new Product(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice());
//        Coupon coupon = restTemplate.getForObject("http://DISCOUNT/api/v1/discount/{code}", Coupon.class, product.getCode());
        CouponDTO couponDTO =  Objects.requireNonNull(webClient.get().uri("api/v1/discount/{code}", product.getCode())
                .retrieve().toEntity(CouponDTO.class).block()).getBody();

        webClient.get().uri("api/v1/discount/" + product.getCode()).retrieve().bodyToMono(CouponDTO.class).block();
//        CouponDTO couponDTO = couponClient.findByCode(product.getCode());
        BigDecimal subtract = new BigDecimal("100").subtract(couponDTO.getDiscount());
        product.setPrice(subtract.multiply(product.getPrice()).divide(new BigDecimal("100")));

        ProductResponse productResponse = mapper.map(productRepository.save(product), ProductResponse.class);

        LoggerDTO logger = new LoggerDTO();
        logger.setSender("product");
        logger.setMessage(productResponse.toString());
        logger.setLocalDateTime(LocalDateTime.now());
        createLog(logger);
        return productResponse;
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
