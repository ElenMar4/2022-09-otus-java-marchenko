package ru.otus.service;

import ru.otus.model.Client;

import java.util.List;

public interface ServiceClient {
    void createClient(String name, String address, List<String> strPhones);
    List<Client> getAll();
}
