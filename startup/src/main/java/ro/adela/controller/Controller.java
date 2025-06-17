package ro.adela.controller;

import interfaces.AmountAccount;
import jakarta.xml.bind.JAXBException;
import readobject.AddRemoveMoneyReadObject;
import readobject.CreateAccountReadObject;
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
public class Controller implements IAlfaInterface, IBetaInterface, IRestExceptionHandler {

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

    @PostMapping(value = "/add-money", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AmountAccount> addMoneyOption(@RequestBody AddRemoveMoneyReadObject addMoneyReadObject) throws IOException, JsonProviderException, JAXBException {
        AmountAccount result = this.service.addAmount(addMoneyReadObject.getAccountNumber(), addMoneyReadObject.getAmount(), addMoneyReadObject.getOperationDate());

        if (result == null) {
            System.out.println(String.format("The account %s cannot be found", addMoneyReadObject.getAccountNumber()));
        } else {
            System.out.println(String.format("The balance of the account for %s is %f", result.getAccountHolderName(), result.getBalance()));
        }

        return ResponseEntity.ok(result);
    }
}