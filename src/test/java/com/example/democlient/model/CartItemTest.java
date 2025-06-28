package com.example.democlient.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class CartItemTest {

    @Test
    void testCartItemModel() {
        CartItem item = new CartItem();
        assertNull(item.getId());
        assertNull(item.getCart());
        assertNull(item.getProduct());
        assertEquals(0, item.getQuantity());

        CartItem item2 = new CartItem(null, null, 3);
        assertEquals(3, item2.getQuantity());
    }
}
