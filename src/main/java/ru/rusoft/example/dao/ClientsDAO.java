package ru.rusoft.example.dao;

import ru.rusoft.example.model.Client;

public interface ClientsDAO {

    void add(Client client);

    void delete(Client client);

    Client findByLogin(String login);

}
