package com.example.democlient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.democlient.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
