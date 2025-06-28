package com.example.democlient.service;

import com.example.democlient.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> listAll();
    OrderItem getOrderItemById(Long id);
    OrderItem create(OrderItem orderItem);
    OrderItem update(OrderItem orderItem);
    void deleteById(Long id);
}
