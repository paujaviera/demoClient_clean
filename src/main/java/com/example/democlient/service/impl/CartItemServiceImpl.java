package com.example.democlient.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.democlient.service.CartItemService;

import com.example.democlient.model.CartItem;
import com.example.democlient.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> listAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public Optional<CartItem> getById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public CartItem create(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

}
