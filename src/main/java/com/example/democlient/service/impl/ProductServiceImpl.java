package com.example.democlient.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.democlient.model.Product;
import com.example.democlient.repository.ProductRepository;
import com.example.democlient.service.ProductService;

@Service

public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
    return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> bulkCreate(List<Product> products) {
        return productRepository.saveAll(products);
    }

}
