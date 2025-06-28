package com.example.democlient.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void testSetAndGetId() {
        product.setId(1L);
        assertEquals(1L, product.getId());
    }

    @Test
    void testSetAndGetName() {
        product.setName("Producto A");
        assertEquals("Producto A", product.getName());
    }

    @Test
    void testSetAndGetDescription() {
        product.setDescription("Descripción del producto");
        assertEquals("Descripción del producto", product.getDescription());
    }

    @Test
    void testSetAndGetPrice() {
        product.setPrice(9900.0);
        assertEquals(9900.0, product.getPrice());
    }

    @Test
    void testSetAndGetStock() {
        product.setStock(50);
        assertEquals(50, product.getStock());
    }
}
