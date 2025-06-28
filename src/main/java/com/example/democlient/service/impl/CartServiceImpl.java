package com.example.democlient.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.democlient.model.Cart;
import com.example.democlient.repository.CartRepository;
import com.example.democlient.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    // Constructor explícito para permitir la inyección de dependencias en tests
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> listAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public int calculateTotal(int price, int quantity) {
        return price * quantity;
    }
}
