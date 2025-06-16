package ro.adela.controller;

import ro.adela.entity.CreateAccountReadObject;
import jakarta.xml.bind.JAXBException;
import ro.adela.IAlfaInterface;
import ro.adela.IBetaInterface;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.service.AbstractService;

import java.io.IOException;

@RestController
public class Controller implements IAlfaInterface, IBetaInterface {

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