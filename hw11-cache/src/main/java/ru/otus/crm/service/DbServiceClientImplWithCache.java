package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.Cache;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImplWithCache implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImplWithCache.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private Cache <Long, Client> cache = new MyCache<>();

    public DbServiceClientImplWithCache(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
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
                cache.put(clientCloned.getId(), clientCloned);
                log.info("Client add in cache");
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            cache.put(clientCloned.getId(), clientCloned);
            log.info("Client add in cache");
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (cache != null && cache.checkPresenceIdInCache(id)) {
            log.info("Client with id=" + id + " find in cache");
            return Optional.ofNullable(cache.get(id));
        } else {
            log.info("Client with id=" + id + " not found in cache.");
            return transactionManager.doInReadOnlyTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                clientOptional.ifPresent(client -> cache.put(client.getId(), client.clone()));
                log.info("client: {}", clientOptional);
                return clientOptional;
            });
        }
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            clientList.forEach(client -> cache.put(client.getId(), client.clone()));
            return clientList;
        });
    }
}