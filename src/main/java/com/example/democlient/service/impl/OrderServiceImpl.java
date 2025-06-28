package com.example.democlient.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.democlient.model.Order;
import com.example.democlient.repository.OrderRepository;
import com.example.democlient.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null); 
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        return orderRepository.save(order); 
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id); 
    }

    @Override
    public List<Order> getOrdersByIdClient(Long idClient) {
        return orderRepository.findByClient_IdClient(idClient); 
    }

}
