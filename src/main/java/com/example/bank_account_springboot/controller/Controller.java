package com.example.bank_account_springboot.controller;

import com.example.bank_account_springboot.entity.CreateAccountReadObject;
import jakarta.xml.bind.JAXBException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.service.AbstractService;

import java.io.IOException;
import java.util.Map;

@RestController
public class Controller {

    private AbstractService service;

    public Controller(AbstractService service) {
        this.service = service;
    }

    // GET API to fetch all details
    @GetMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello World";
    }

    @PostMapping(value = "/create-account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateAccountReadObject> createAccountOption(@RequestBody CreateAccountReadObject createAccountReadObject) throws IOException, JsonProviderException, JAXBException {
        BankAccountDto accountDto = new BankAccountDto(createAccountReadObject.getAccountNumber(), createAccountReadObject.getAccountHolderName(), 0.0, createAccountReadObject.getBirtDate(), createAccountReadObject.getStartDate());
        System.out.println("Savings account balance: " + accountDto.getBalance()); // Check balance

        this.service.addAccount(accountDto);

        return ResponseEntity.ok(createAccountReadObject);
    }
}