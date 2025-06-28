package com.example.democlient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.democlient.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

