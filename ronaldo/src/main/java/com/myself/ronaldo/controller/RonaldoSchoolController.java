package com.myself.ronaldo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RonaldoSchoolController {
    @GetMapping(path = "/webhook/studentAdded/{name}")
    public @ResponseBody String studentAdded(@PathVariable String name) {
        System.out.println("Student name: "+ name);
        return "Webhook received";
    }
}
