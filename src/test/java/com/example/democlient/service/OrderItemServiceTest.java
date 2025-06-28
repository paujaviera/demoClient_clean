package com.example.democlient.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.democlient.model.Order;
import com.example.democlient.model.OrderItem;
import com.example.democlient.model.Product;
import com.example.democlient.repository.OrderItemRepository;
import com.example.democlient.service.impl.OrderItemServiceImpl;

public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private OrderItem sampleItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Product product = new Product();
        product.setId(1L);
        product.setName("Producto A");
        product.setPrice(1000.0);
        product.setStock(10);

        Order order = new Order();
        order.setId(1L);

        sampleItem = new OrderItem();
        sampleItem.setId(1L);
        sampleItem.setProduct(product);
        sampleItem.setOrder(order);
        sampleItem.setPrice(1000.0);
        sampleItem.setQuantity(2);
    }

    @Test
    public void testListAll() {
        List<OrderItem> list = Arrays.asList(sampleItem);
        when(orderItemRepository.findAll()).thenReturn(list);

        List<OrderItem> result = orderItemService.listAll();
        assertEquals(1, result.size());
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));

        OrderItem result = orderItemService.getOrderItemById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderItemRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreate() {
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(sampleItem);

        OrderItem result = orderItemService.create(sampleItem);
        assertEquals(2, result.getQuantity());
        verify(orderItemRepository).save(sampleItem);
    }

    @Test
    public void testUpdate() {
        sampleItem.setQuantity(5);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(sampleItem);

        OrderItem result = orderItemService.update(sampleItem);
        assertEquals(5, result.getQuantity());
        verify(orderItemRepository).save(sampleItem);
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        doNothing().when(orderItemRepository).deleteById(id);

        orderItemService.deleteById(id);
        verify(orderItemRepository).deleteById(id);
    }
}
