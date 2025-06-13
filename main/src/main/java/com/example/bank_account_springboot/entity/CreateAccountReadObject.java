package com.example.bank_account_springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CreateAccountReadObject {
    private Integer accountNumber;
    private String accountHolderName;
    private Double balance;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birtDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;
}
