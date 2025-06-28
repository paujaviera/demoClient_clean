package com.example.democlient.service;

import com.example.democlient.model.Cart;
import com.example.democlient.repository.CartRepository;
import com.example.democlient.service.impl.CartServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private CartRepository cartRepository;
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        cartRepository = Mockito.mock(CartRepository.class);
        cartService = new CartServiceImpl(cartRepository);
    }

    @Test
    public void testListAllCarts() {
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        List<Cart> mockList = Arrays.asList(cart1, cart2);

        when(cartRepository.findAll()).thenReturn(mockList);

        List<Cart> result = cartService.listAll();
        assertEquals(2, result.size());
        verify(cartRepository, times(1)).findAll();
    }

    @Test
    public void testCreateCart() {
        Cart cart = new Cart();
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.createCart(cart);
        assertNotNull(result);
        verify(cartRepository).save(cart);
    }

    @Test
    public void testGetCartById() {
        Cart cart = new Cart();
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Optional<Cart> result = cartService.getCartById(1L);
        assertTrue(result.isPresent());
        verify(cartRepository).findById(1L);
    }

    @Test
    public void testDeleteCart() {
        doNothing().when(cartRepository).deleteById(1L);

        cartService.deleteCart(1L);
        verify(cartRepository).deleteById(1L);
    }

    @Test
    public void testCalculateTotal() {
        int result = cartService.calculateTotal(100, 3);
        assertEquals(300, result);
    }
}
