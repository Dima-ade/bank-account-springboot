package ro.adela.controller;

import interfaces.AmountAccount;
import jakarta.websocket.server.PathParam;
import jakarta.xml.bind.JAXBException;
import readobject.AddRemoveMoneyReadObject;
import readobject.CreateAccountReadObject;
import ro.adela.IAlfaInterface;
import ro.adela.IBetaInterface;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.adela.IRestExceptionHandler;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.OutputSummaryAmountDto;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.service.AbstractService;
import ro.adela.bank.utils.CsvFileWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@RestController
public class Controller implements IAlfaInterface, IBetaInterface, IRestExceptionHandler {

    private AbstractService service;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    @PostMapping(value = "/remove-money", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AmountAccount> removeMoneyOption(@RequestBody AddRemoveMoneyReadObject removeMoneyReadObject) throws IOException, JsonProviderException, JAXBException {
        AmountAccount result = this.service.removeAmount(removeMoneyReadObject.getAccountNumber(), removeMoneyReadObject.getAmount(), removeMoneyReadObject.getOperationDate());
        if (result == null) {
            System.out.println(String.format("The account %s cannot be found", removeMoneyReadObject.getAccountNumber()));
        } else {
            System.out.println(String.format("The balance of the account for %s is %f", result.getAccountHolderName(), result.getBalance()));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/filter-amounts-by-month", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OutputSummaryAmountDto>> filterAmountsByMonthOption(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException, JAXBException {
        LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);
        LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.service.filterAmountsByMonths(null, startDateFormatted, endDateFormatted);
        CsvFileWriter.writeCsvFile(true, "output-summary-by-months.csv", outputSummaryAmountDtos);

        return ResponseEntity.ok(outputSummaryAmountDtos);
    }

    @GetMapping(value = "/filter-amounts-by-month-and-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OutputSummaryAmountDto>> filterAmountsByMonthAndAccountOption(@RequestParam("accountNumber") Integer accountNumber, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException, JAXBException {
        LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);
        LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.service.filterAmountsByMonths(accountNumber, startDateFormatted, endDateFormatted);
        CsvFileWriter.writeCsvFile(true, "output-summary-by-months-and-account.csv", outputSummaryAmountDtos);

        return ResponseEntity.ok(outputSummaryAmountDtos);
    }

    @GetMapping(value = "/filter-amounts-by-weeks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OutputSummaryAmountDto>> filterAmountsByWeeksOption(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException, JAXBException {
        LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);
        LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.service.filterAmountsByWeeks(null, startDateFormatted, endDateFormatted);
        CsvFileWriter.writeCsvFile(true, "output-summary-by-weeks.csv", outputSummaryAmountDtos);

        return ResponseEntity.ok(outputSummaryAmountDtos);
    }

    @GetMapping(value = "/filter-amounts-by-week-and-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OutputSummaryAmountDto>> filterAmountsByWeeksAndAccountOption(@RequestParam("accountNumber") Integer accountNumber, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException, JAXBException {
        LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);
        LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.service.filterAmountsByWeeks(accountNumber, startDateFormatted, endDateFormatted);
        CsvFileWriter.writeCsvFile(true, "output-summary-by-weeks-and-account.csv", outputSummaryAmountDtos);

        return ResponseEntity.ok(outputSummaryAmountDtos);
    }
}