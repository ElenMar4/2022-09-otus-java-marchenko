package ru.otus.services;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.security.RolePrincipal;
import org.eclipse.jetty.security.UserPrincipal;
import ru.otus.data.crm.service.DBServiceClient;


import java.util.List;

public class InMemoryLoginServiceImpl extends AbstractLoginService {

    private final DBServiceClient dbServiceClient;

    public InMemoryLoginServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }


    @Override
    protected List<RolePrincipal> loadRoleInfo(UserPrincipal userPrincipal) {
        //return List.of(new RolePrincipal("user"));
        return null;
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
//        System.out.println(String.format("InMemoryLoginService#loadUserInfo(%s)", login));
//        Optional<User> dbUser = userDao.findByLogin(login);
//        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
        return null;
    }
}
