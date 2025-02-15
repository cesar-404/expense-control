package com.evangelion.expensecontrol.controllers;

import com.evangelion.expensecontrol.dtos.person.PersonWithTotalDTO;
import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.services.PersonService;
import com.evangelion.expensecontrol.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PersonService personService;

    public TransactionController(TransactionService transactionService, PersonService personService) {
        this.transactionService = transactionService;
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO transactionDTO) {
        var transaction = transactionService.createTransaction(transactionDTO);
        return ResponseEntity.ok().body(transaction);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TransactionDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/totals")
    public ResponseEntity<List<PersonWithTotalDTO>> getPersonsTotals() {
        List<PersonWithTotalDTO> personsWithTotals = personService.getPersonsWithTotals();
        TotalTransactionDTO totalTransactionDTO = transactionService.getTotalGeneral();

        personsWithTotals.add(new PersonWithTotalDTO("Total", totalTransactionDTO));
        return ResponseEntity.ok().body(personsWithTotals);
    }
}