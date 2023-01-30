package com.mongodb.example.mongodb.controller;


import com.mongodb.example.mongodb.model.ProductResult;
import com.mongodb.example.mongodb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public List<ProductResult> getAllPosts()
    {
        return productRepository.getProducts("Hay","en");
    }

}
