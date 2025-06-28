package com.example.democlient.service;

import java.util.List;

import com.example.democlient.model.Product;

public interface ProductService {
    List<Product> listAll();
    Product getProductById(Long id);
    Product create(Product product);
    Product update(Product product);
    void deleteById(Long id);
    List<Product> bulkCreate(List<Product> products);

}
