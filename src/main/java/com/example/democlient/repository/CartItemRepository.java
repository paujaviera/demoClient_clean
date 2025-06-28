package com.example.democlient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.democlient.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
