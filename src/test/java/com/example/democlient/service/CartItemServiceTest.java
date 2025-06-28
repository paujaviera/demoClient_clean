package com.example.democlient.service;


import com.example.democlient.model.CartItem;
import com.example.democlient.repository.CartItemRepository;
import com.example.democlient.service.impl.CartItemServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAll() {
        List<CartItem> items = Arrays.asList(new CartItem(), new CartItem());
        when(cartItemRepository.findAll()).thenReturn(items);
        assertEquals(2, cartItemService.listAll().size());
    }

    // Otros tests igual que antes...
}
