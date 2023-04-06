package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.service.ServiceClient;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {

    private final ServiceClient serviceClient;

    public ClientController(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/clients")
    public String getClientsList(Model model) {
        model.addAttribute("clients", serviceClient.getAll());
        return "clients";
    }

    @PostMapping("/clients/add")
    public String postClient(@RequestParam String name,
                             @RequestParam String address,
                             @RequestParam String phone1,
                             @RequestParam String phone2,
                             @RequestParam String phone3) {

        List<String> strPhones = new ArrayList<>();
        strPhones.add(phone1);
        if (!phone2.isEmpty()) {
            strPhones.add(phone2);
        }
        if (!phone3.isEmpty()) {
            strPhones.add(phone3);
        }
        serviceClient.createClient(name, address, strPhones);
        return "clients";
    }
}
