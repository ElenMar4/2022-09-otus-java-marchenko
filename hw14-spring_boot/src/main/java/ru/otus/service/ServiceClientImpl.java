package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientRepository;
import ru.otus.repository.PhoneRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceClientImpl implements ServiceClient {
    @Autowired
    private final ClientRepository clientRepository;
    @Autowired
    private final PhoneRepository phonesRepository;
    @Autowired
    private final AddressRepository addressRepository;


    @Override
    @Transactional
    public void createClient(String name, String address, List<String> strPhones) {
        Client client = clientRepository.save(new Client(name));
        addressRepository.save(new Address(address, client.getId()));
        for (String number : strPhones) {
            phonesRepository.save(new Phone(number, client.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAll() {
        List<Client> list = new ArrayList<>();
        clientRepository.findAll().forEach(list::add);
        return list;
    }
}