package com.example.democlient.service;

import java.util.List;
import java.util.Optional;

import com.example.democlient.model.CartItem;

public interface CartItemService {
    List<CartItem> listAll();

    Optional<CartItem> getById(Long id);

    CartItem create(CartItem cartItem);

    CartItem update(CartItem cartItem);

    void deleteById(Long id);

}
