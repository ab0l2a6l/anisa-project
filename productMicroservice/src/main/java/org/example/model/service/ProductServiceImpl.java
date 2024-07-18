package org.example.model.service;

import org.example.dto.ProductResponse;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.example.dto.ProductRequest;
import org.example.exception.NotFoundProduct;
import org.example.model.entity.Coupon;
import org.example.model.entity.Product;
import org.example.model.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = mapper.map(productRequest, Product.class);
//        Product product = new Product(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice());
        Coupon coupon = restTemplate.getForObject("http://DISCOUNT/api/v1/discount/{code}", Coupon.class, product.getCode());

        BigDecimal subtract = new BigDecimal("100").subtract(coupon.getDiscount());
        product.setPrice(subtract.multiply(product.getPrice()).divide(new BigDecimal("100")));

        return mapper.map(productRepository.save(product), ProductResponse.class);
    }

    @Override
    public ProductResponse findById(Long id) throws NotFoundProduct {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundProduct("not found product");
        }
        return mapper.map(product.get(), ProductResponse.class);
    }
}
