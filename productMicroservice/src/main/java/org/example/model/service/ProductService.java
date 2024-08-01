package org.example.model.service;


import org.example.dto.ProductRequest;
import org.example.dto.ProductResponse;
import org.example.exception.NotFoundProduct;
import org.example.notification.LoggerDTO;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest) throws NotFoundProduct;

    ProductResponse findById(Long id) throws NotFoundProduct;

    void createLog(LoggerDTO logger);
}
