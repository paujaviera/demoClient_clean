package com.example.democlient.service;

import java.util.List;

import com.example.democlient.model.Order;

public interface OrderService {
    List<Order> listAll();
    Order getOrderById(Long id);
    Order create(Order order);
    Order update(Order order);
    void deleteById(Long id);
    List<Order> getOrdersByIdClient(Long idClient);

}