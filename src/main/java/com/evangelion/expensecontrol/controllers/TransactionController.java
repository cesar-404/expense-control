package com.evangelion.expensecontrol.controllers;

import com.evangelion.expensecontrol.dtos.person.PersonWithTotalDTO;
import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.services.PersonService;
import com.evangelion.expensecontrol.services.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions") // Define o caminho base para as requisições relacionadas às transações
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class); // Logger para registrar eventos e informações
    private final TransactionService transactionService; // Serviço para gerenciar transações
    private final PersonService personService; // Serviço para gerenciar informações de pessoas

    public TransactionController(TransactionService transactionService, PersonService personService) {
        this.transactionService = transactionService; // Injeção do serviço de transações
        this.personService = personService; // Injeção do serviço de pessoas
    }

    @PostMapping // Mapeia a requisição POST para a criação de uma nova transação
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO transactionDTO) {
        logger.info("Creating a new transaction with details: {}", transactionDTO);
        var transaction = transactionService.createTransaction(transactionDTO); // Cria a transação usando o serviço
        logger.info("Transaction created successfully with ID: {}", transaction.personId());
        return ResponseEntity.ok().body(transaction); // Retorna a transação criada como resposta
    }

    @GetMapping("/list") // Mapeia a requisição GET para obter todas as transações
    public ResponseEntity<List<TransactionDTO>> getAll() {
        logger.info("Fetching all transactions");
        List<TransactionDTO> transactions = transactionService.getAllTransactions(); // Obtém todas as transações
        logger.info("Retrieved {} transactions", transactions.size());
        return ResponseEntity.ok(transactions); // Retorna a lista de transações como resposta
    }

    @GetMapping("/totals") // Mapeia a requisição GET para obter os totais de transações das pessoas
    public ResponseEntity<List<PersonWithTotalDTO>> getPersonsTotals() {
        logger.info("Fetching persons with total transaction details");
        List<PersonWithTotalDTO> personsWithTotals = personService.getPersonsWithTotals(); // Obtém a lista de pessoas com seus totais de transações
        TotalTransactionDTO totalTransactionDTO = transactionService.getTotalGeneral(); // Obtém o total geral de transações

        // Adiciona os totais gerais na lista de pessoas com totais
        personsWithTotals.add(new PersonWithTotalDTO("Total", totalTransactionDTO));
        logger.info("Total transaction details added to the list");
        return ResponseEntity.ok().body(personsWithTotals); // Retorna a lista de pessoas com os totais como resposta
    }
}