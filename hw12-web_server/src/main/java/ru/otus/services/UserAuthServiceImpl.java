package ru.otus.services;


import ru.otus.data.crm.service.DBServiceClient;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceClient dbServiceClient;

    public UserAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return login.equals("admin") && password.equals("11111");

//        return dbServiceClient.findByLogin(login)
//                .map(user -> user.getPassword().equals(password))
//                .orElse(false);
    }

}
