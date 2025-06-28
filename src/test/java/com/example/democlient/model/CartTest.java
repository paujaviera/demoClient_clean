package com.example.democlient.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartTest {

    private Cart cart;
    private Product product;

    @BeforeEach
    public void setUp() {
        cart = new Cart();  
        product = new Product();  
        product.setId(1L);  
        product.setName("Product 1");  
        product.setDescription("Description"); 
        product.setPrice(100.0);  
        product.setStock(10);  
    }

    @Test
    public void testAddProduct() {
        cart.addProduct(product, 2); 

        assertEquals(1, cart.getItems().size());  
        assertEquals(2, cart.getItems().get(0).getQuantity());  
    }

    @Test
    public void testRemoveProduct() {
        cart.addProduct(product, 2);  
        cart.removeProduct(product.getId());  

        assertTrue(cart.getItems().isEmpty());  
    }

    @Test
    public void testGetTotal() {
        cart.addProduct(product, 2); 

        double total = cart.getTotal(); 

        assertEquals(200.0, total, "El total debe ser 200.0"); 
    }
}