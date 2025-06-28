package com.example.democlient.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.democlient.model.Client;
import com.example.democlient.repository.ClientRepository;
import com.example.democlient.service.impl.ClientServiceImpl;

public class ClientServiceTest {

    private ClientRepository clientRepository;
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new ClientServiceImpl();
        ReflectionTestUtils.setField(clientService, "clientRepository", clientRepository);
    }

    @Test
    public void testListAllClients() {
        Client client1 = new Client();
        client1.setFullName("Cliente Uno");

        Client client2 = new Client();
        client2.setFullName("Cliente Dos");

        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));

        List<Client> clients = clientService.listAll();

        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testListById_found() {
        Client client = new Client();
        client.setFullName("Nombre Cliente");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client result = clientService.listById(1L);

        assertNotNull(result);
        assertEquals("Nombre Cliente", result.getFullName());
    }

    @Test
    public void testListById_notFound() {
        when(clientRepository.findById(2L)).thenReturn(Optional.empty());

        Client result = clientService.listById(2L);

        assertNull(result);
    }

    @Test
    public void testCreateClient() {
        Client client = new Client();
        client.setFullName("Nuevo Cliente");

        when(clientRepository.save(client)).thenReturn(client);

        Client created = clientService.create(client);

        assertEquals("Nuevo Cliente", created.getFullName());
        verify(clientRepository).save(client);
    }

    @Test
    public void testUpdateClient() {
        Client client = new Client();
        client.setFullName("Cliente Actualizado");

        when(clientRepository.save(client)).thenReturn(client);

        Client updated = clientService.update(client);

        assertEquals("Cliente Actualizado", updated.getFullName());
        verify(clientRepository).save(client);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        clientService.deleteById(id);

        verify(clientRepository, times(1)).deleteById(id);
    }

    @Test
    public void testBulkCreate() {
        Client c1 = new Client(); c1.setFullName("A");
        Client c2 = new Client(); c2.setFullName("B");
        List<Client> list = Arrays.asList(c1, c2);

        when(clientRepository.saveAll(list)).thenReturn(list);

        List<Client> saved = clientService.bulkCreate(list);

        assertEquals(2, saved.size());
        verify(clientRepository).saveAll(list);
    }
}
