package com.example.democlient.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.democlient.model.Client;
import com.example.democlient.repository.ClientRepository;
import com.example.democlient.service.ClientService;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> listAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client listById(Long idClient) {
        Optional<Client> client = clientRepository.findById(idClient);
        return client.orElse(null); 
    }

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteById(Long idClient) {
        clientRepository.deleteById(idClient);
    }

    @Override
    public List<Client> bulkCreate(List<Client> clients) {
        return clientRepository.saveAll(clients);
    }

}
