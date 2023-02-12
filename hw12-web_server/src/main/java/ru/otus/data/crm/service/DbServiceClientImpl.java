package ru.otus.data.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.data.core.repository.DataTemplate;
import ru.otus.data.core.sessionmanager.TransactionManager;
import ru.otus.data.crm.model.Client;


import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

//    @Override
//    public Optional<Client> getClient(long id) {
//        return transactionManager.doInReadOnlyTransaction(session -> {
//            var clientOptional = clientDataTemplate.findById(session, id);
//            log.info("client: {}", clientOptional);
//            return clientOptional;
//        });
//    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    @Override
    public Optional<Client> findByLogin(String login) {
        return transactionManager.doInTransaction(session -> {
            var clientOptional = clientDataTemplate.findByField(session, "login", login);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }
}