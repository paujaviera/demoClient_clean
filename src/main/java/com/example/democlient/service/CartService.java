package com.example.democlient.service;

import java.util.List;
import java.util.Optional;

import com.example.democlient.model.Cart;

public interface CartService {
    List<Cart> listAll(); 
    Cart createCart(Cart cart);
    Optional<Cart> getCartById(Long id);
    Cart updateCart(Cart cart);
    void deleteCart(Long id);
    int calculateTotal(int price, int quantity);

}
