package ru.otus.data.crm.service;



import ru.otus.data.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

//    Optional<Client> getClient(long id);

    List<Client> findAll();

    Optional<Client> findByLogin(String login);
}
