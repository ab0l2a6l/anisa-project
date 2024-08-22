package org.example.model.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.example.discount.CouponClient;
import org.example.discount.CouponDTO;
import org.example.notification.LoggerClient;
import org.example.dto.ProductResponse;
import org.example.notification.LoggerDTO;
import org.modelmapper.ModelMapper;
import org.example.dto.ProductRequest;
import org.example.exception.NotFoundProduct;
import org.example.model.entity.Product;
import org.example.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;

    private final LoggerClient loggerClient;

    private final CouponClient couponClient;
    private final RestClient.Builder restClient;
    private final WebClient.Builder webClient;

    public ProductServiceImpl(ProductRepository productRepository, RestTemplate restTemplate, ModelMapper mapper, @Qualifier("org.example.notification.LoggerClient") LoggerClient loggerClient, @Qualifier("org.example.discount.CouponClient") CouponClient couponClient, RestClient.Builder restClient, WebClient.Builder webClient) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.loggerClient = loggerClient;
        this.couponClient = couponClient;
        this.restClient = restClient;
        this.webClient = webClient;
    }

    @Override
    @Retry(name = "createProductRetry", fallbackMethod = "createProductFallBack")
//    @CircuitBreaker(name = "myClient", fallbackMethod = "createProductFallBack") // priority circuitBreaker greater than Retry
    public ProductResponse createProduct(ProductRequest productRequest) throws NotFoundProduct {

        log.info("invoke createProduct");

        Product product = mapper.map(productRequest, Product.class);
        if (true)
            throw new NotFoundProduct("fsdf");
//  RestTemplate
//        CouponDTO couponDTO = restTemplate.getForObject("http://DISCOUNT/api/v1/coupon/find/{code}", CouponDTO.class, product.getCode());

//  WebClient
//        CouponDTO couponDTO =  Objects.requireNonNull(webClient.build().get()
//                .uri("http://DISCOUNT/api/v1/coupon/find/{code}", product.getCode())
//                .retrieve().toEntity(CouponDTO.class).block()).getBody();

//  RestClient
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
//    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ProductResponse createProductFallBack(Throwable throwable) {
        log.info("invoke createProductFallBack");
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
