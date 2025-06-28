package com.example.democlient.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private Order order;
    private Client client;

    @BeforeEach
    public void setUp() {
        order = new Order();
        client = new Client();
        client.setIdClient(1L);
        client.setFullName("Juan");
        client.setPassword("1234");
    }

    @Test
    public void testSetAndGetId() {
        order.setId(10L);
        assertEquals(10L, order.getId());
    }

    @Test
    public void testSetAndGetOrderDate() {
        LocalDateTime now = LocalDateTime.now();
        order.setOrderDate(now);
        assertEquals(now, order.getOrderDate());
    }

    @Test
    public void testSetAndGetStatus() {
        order.setStatus("En proceso");
        assertEquals("En proceso", order.getStatus());
    }

    @Test
    public void testSetAndGetTotalAmount() {
        order.setTotalAmount(9990);
        assertEquals(9990, order.getTotalAmount());
    }

    @Test
    public void testSetAndGetClient() {
        order.setClient(client);
        assertNotNull(order.getClient());
        assertEquals("Juan", order.getClient().getFullName());
    }

    @Test
    public void testSetAndGetItems() {
        OrderItem item = new OrderItem();
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        assertEquals(1, order.getItems().size());
        assertSame(item, order.getItems().get(0));
    }
}
