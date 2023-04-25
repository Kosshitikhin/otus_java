package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceClientWithCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbServiceDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var clients = createClients(1000);
        var dbServiceClient = createDBServiceClient();
        var cache = new MyCache<Long, Client>();
        var dbCacheServiceClient = new DbServiceClientWithCache(dbServiceClient, cache);

        System.out.println("Время выполнения с кэшем: " + runTest(dbCacheServiceClient, clients));
        System.out.println("\n");
        System.out.println("Время выполнения без кэша: " + runTest(dbServiceClient, clients));
        System.out.println("\n");
    }

    private static DBServiceClient createDBServiceClient() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    private static List<Client> createClients(int count) {
        var result = new ArrayList<Client>(count);
        for (int i = 1; i <= count; i++) {
            result.add(new Client(null, "Client " + i, new Address(null, "Address: " + i),
                    List.of(new Phone(null, String.valueOf(1000 + i)), new Phone(null, String.valueOf(1001 + i)))));
        }
        return result;
    }

    private static long runTest(DBServiceClient serviceClient, List<Client> clients) {
        var start = new Date().getTime();

        List<Long> identifiers = new ArrayList<>();

        for (Client client : clients) {
            identifiers.add(serviceClient.saveClient(client).getId());
        }

        for (Long id : identifiers) {
            serviceClient.getClient(id);
        }

        var finish = new Date().getTime();

        return finish - start;
    }
}
