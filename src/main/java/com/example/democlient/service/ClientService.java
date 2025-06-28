package com.example.democlient.service;

import java.util.List;

import com.example.democlient.model.Client;

public interface ClientService {
    List<Client> listAll();
    Client listById(Long idClient);
    Client create(Client client);
    Client update(Client client);
    void deleteById(Long idClient);
    List<Client> bulkCreate(List<Client> clients);
}

