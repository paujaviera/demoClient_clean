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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.democlient.model.Product;
import com.example.democlient.repository.ProductRepository;
import com.example.democlient.service.impl.ProductServiceImpl;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAll() {
        List<Product> mockProducts = Arrays.asList(
            new Product(), new Product()
        );

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> result = productService.listAll();
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_found() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProductById_notFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Product result = productService.getProductById(2L);
        assertNull(result);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setName("Producto Nuevo");

        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.create(product);
        assertEquals("Producto Nuevo", saved.getName());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Actualizado");

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.update(product);
        assertEquals("Actualizado", result.getName());
    }

    @Test
    void testDeleteById() {
        Long id = 5L;

        productService.deleteById(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testBulkCreate() {
        List<Product> products = Arrays.asList(new Product(), new Product());

        when(productRepository.saveAll(products)).thenReturn(products);

        List<Product> result = productService.bulkCreate(products);
        assertEquals(2, result.size());
        verify(productRepository).saveAll(products);
    }
}
