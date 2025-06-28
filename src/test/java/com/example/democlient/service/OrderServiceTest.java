package com.example.democlient.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.democlient.model.Order;
import com.example.democlient.repository.OrderRepository;
import com.example.democlient.service.impl.OrderServiceImpl;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAllOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.listAll();

        assertEquals(2, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void testGetOrderById_found() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetOrderById_notFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Order result = orderService.getOrderById(999L);

        assertNull(result);
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        order.setStatus("PENDIENTE");

        when(orderRepository.save(order)).thenReturn(order);

        Order saved = orderService.create(order);

        assertEquals("PENDIENTE", saved.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("ENVIADO");

        when(orderRepository.save(order)).thenReturn(order);

        Order updated = orderService.update(order);

        assertEquals("ENVIADO", updated.getStatus());
    }

    @Test
    void testDeleteOrderById() {
        Long id = 10L;

        orderService.deleteById(id);

        verify(orderRepository).deleteById(id);
    }

    @Test
    void testGetOrdersByClientId() {
        Long clientId = 100L;
        List<Order> orders = Arrays.asList(new Order(), new Order());

        when(orderRepository.findByClient_IdClient(clientId)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByIdClient(clientId);

        assertEquals(2, result.size());
        verify(orderRepository).findByClient_IdClient(clientId);
    }
}
