package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductRequest;
import org.example.dto.ProductResponse;
import org.example.exception.NotFoundProduct;
import org.example.model.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) throws NotFoundProduct {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("id") Long id) throws NotFoundProduct {
        return ResponseEntity.ok(productService.findById(id));
    }
}
