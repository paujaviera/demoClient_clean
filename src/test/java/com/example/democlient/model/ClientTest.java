package com.example.democlient.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    void testSetAndGetIdClient() {
        Client client = new Client();
        client.setIdClient(100L);
        assertEquals(100L, client.getIdClient());
    }

    @Test
    void testSetAndGetFullName() {
        Client client = new Client();
        client.setFullName("María González");
        assertEquals("María González", client.getFullName());
    }

    @Test
    void testSetAndGetPassword() {
        Client client = new Client();
        client.setPassword("secreto123");
        assertEquals("secreto123", client.getPassword());
    }

    @Test
    void testInitialValuesAreNull() {
        Client client = new Client();
        assertNull(client.getIdClient());
        assertNull(client.getFullName());
        assertNull(client.getPassword());
    }
}
