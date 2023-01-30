package com.mongodb.example.mongodb.repository;

import com.mongodb.example.mongodb.model.Product;
import com.mongodb.example.mongodb.model.ProductResult;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository {

    List<ProductResult> getProducts(String text,String locationLanguage);

}

