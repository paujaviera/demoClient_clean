package com.example.democlient.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.democlient.model.OrderItem;
import com.example.democlient.repository.OrderItemRepository;
import com.example.democlient.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> listAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        return orderItem.orElse(null); // Si no se encuentra el OrderItem, retornamos null
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteById(Long id) {
        orderItemRepository.deleteById(id);
    }

}
