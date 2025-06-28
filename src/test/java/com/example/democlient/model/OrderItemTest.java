package com.example.democlient.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderItemTest {

    private OrderItem orderItem;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100.0);

        order = new Order();
        order.setId(1L);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setPrice(100.0);
        orderItem.setProduct(product);
        orderItem.setOrder(order);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, orderItem.getId());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(100.0, orderItem.getPrice());
        assertEquals(product, orderItem.getProduct());
        assertEquals(order, orderItem.getOrder());
    }
}
