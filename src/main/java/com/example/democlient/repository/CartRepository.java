package com.example.democlient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.democlient.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
