package org.example.model.service;


import org.example.dto.ProductRequest;
import org.example.dto.ProductResponse;
import org.example.exception.NotFoundProduct;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse findById(Long id) throws NotFoundProduct;
}
