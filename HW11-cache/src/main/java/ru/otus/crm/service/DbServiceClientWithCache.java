package ru.otus.crm.service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.cache.HwCache;
import ru.otus.cache.HwListener;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DbServiceClientWithCache implements DBServiceClient, HwListener<Long, Client> {
    private final DBServiceClient dbServiceClient;
    private final HwCache<Long, Client> cache;


    public DbServiceClientWithCache(DBServiceClient dbServiceClient, HwCache<Long, Client> cache) {
        this.dbServiceClient = dbServiceClient;
        this.cache = cache;
        cache.addListener(this);
    }

    @Override
    public Client saveClient(Client client) {
        var result = dbServiceClient.saveClient(client).clone();
        cache.put(result.getId(), result);
        return result;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = cache.get(id);
        if (client == null) {
            log.info("There isn't client in cache. Going to db");
            var optionalClient = dbServiceClient.getClient(id);
            optionalClient.ifPresent(value -> cache.put(value.getId(), value.clone()));
            return optionalClient;
        }
        return Optional.of(client);
    }

    @Override
    public List<Client> findAll() {
        var clientList = dbServiceClient.findAll();
        clientList.forEach(client -> cache.put(client.getId(), client.clone()));
        return clientList;
    }

    @Override
    public void notify(Long key, Client value, String action) {
        log.info("key:{}, value:{}, action: {}", key, value, action);
    }
}
