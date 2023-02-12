package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ru.otus.data.crm.model.Address;
import ru.otus.data.crm.model.Client;
import ru.otus.data.crm.model.Phone;
import ru.otus.data.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_ALL_CLIENTS = "clients";
    private static final String NEW_CLIENT_NAME = "newClientName";
    private static final String NEW_CLIENT_ADDRESS = "newClientAddress";
    private static final String NEW_CLIENT_PHONES = "newClientPhones";


    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;
    private final Gson gson;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient, Gson gson) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        createResponse(resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone(req.getParameter(NEW_CLIENT_PHONES)));
        dbServiceClient.saveClient(new Client(req.getParameter(NEW_CLIENT_NAME),
                new Address(req.getParameter(NEW_CLIENT_ADDRESS)),
                phones));
        createResponse(resp);
    }

    private void createResponse(HttpServletResponse resp) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients =  dbServiceClient.findAll();
        paramsMap.put(TEMPLATE_ATTR_ALL_CLIENTS, clients);
        resp.setContentType("text/html");
        resp.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }
}
