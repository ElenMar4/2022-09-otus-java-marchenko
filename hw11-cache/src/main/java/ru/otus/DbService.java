package ru.otus;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceClientImplWithCache;

import java.util.ArrayList;
import java.util.List;

public class DbService {

    private static final Logger log = LoggerFactory.getLogger(DbService.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        //Меняя имплементацию dbServiceClient, получаем разное время выполнения

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
//        var dbServiceClient = new DbServiceClientImplWithCache(transactionManager, clientTemplate);


        List<Client> listClients = new ArrayList<>();
        for(int i = 0; i < 300; i++){
            String tempNameClient = "Client" + i;
            String tempAddress = "street" + i;
            String tempPhoneOne = i + "-" + i +"-" + i;
            String tempPhoneTwo = i + "-" + i +"-" + i+ "-" + i;
            listClients.add(dbServiceClient.saveClient(new Client(null,
                                                    tempNameClient,
                                                    new Address(null, tempAddress),
                                                    List.of(new Phone(null, tempPhoneOne), new Phone(null, tempPhoneTwo)))));
        }

        long start = System.currentTimeMillis();
        for (Client client : listClients) {
            dbServiceClient.getClient(client.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        }
        long finish = System.currentTimeMillis();

        System.out.println("_____ Время поиска клиентов: " + (finish - start) + " _______");

        System.gc();
    }
}

