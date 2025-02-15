package com.evangelion.expensecontrol.services.impl;

import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.exceptions.PersonNotFoundException;
import com.evangelion.expensecontrol.mappers.TransactionMapper;
import com.evangelion.expensecontrol.models.person.Person;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.models.transaction.TransactionTypeEnum;
import com.evangelion.expensecontrol.repositories.transaction.TransactionRepository;
import com.evangelion.expensecontrol.validator.TransactionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionValidator transactionValidator;

    @InjectMocks
    DefaultTransactionService defaultTransactionService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("Mario Mario", 24);
    }

    @Test
    void shouldCreateTransactionSuccessfully() {
        TransactionDTO transactionDTO = new TransactionDTO("test",
                BigDecimal.valueOf(20), TransactionTypeEnum.RECEITA, 1L);

        Transaction transaction = new Transaction("test", BigDecimal.valueOf(20),
                TransactionTypeEnum.RECEITA, person);

        Transaction savedTransaction = new Transaction(1L, "test", BigDecimal.valueOf(20),
                TransactionTypeEnum.RECEITA, person);

        when(transactionMapper.toEntity(transactionDTO)).thenReturn(transaction);
        doNothing().when(transactionValidator).validateTransaction(transaction);
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(transactionMapper.toDTO(savedTransaction)).thenReturn(transactionDTO);

        TransactionDTO result = defaultTransactionService.createTransaction(transactionDTO);

        assertNotNull(result);
        assertEquals(transactionDTO.description(), result.description());
        assertEquals(transactionDTO.amount(), result.amount());
        assertEquals(transactionDTO.type(), result.type());
        assertEquals(transactionDTO.personId(), result.personId());

        verify(transactionMapper).toEntity(transactionDTO);
        verify(transactionValidator).validateTransaction(transaction);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toDTO(savedTransaction);
    }

    @Test
    void shouldCreateTransactionFailed() {
        TransactionDTO transactionDTO = new TransactionDTO("test", BigDecimal.valueOf(20),
                TransactionTypeEnum.RECEITA, 1L);

        Transaction transaction = new Transaction("test", BigDecimal.valueOf(20),
                TransactionTypeEnum.RECEITA, person);

        when(transactionMapper.toEntity(transactionDTO)).thenReturn(transaction);
        doThrow(new PersonNotFoundException("Person not found")).when(transactionValidator).validateTransaction(transaction);

        assertThrows(PersonNotFoundException.class, () -> {
            defaultTransactionService.createTransaction(transactionDTO);
        });

        verify(transactionMapper).toEntity(transactionDTO);
        verify(transactionValidator).validateTransaction(transaction);
        verifyNoInteractions(transactionRepository);
        verifyNoMoreInteractions(transactionMapper);
    }

    @Test
    void shouldCalculatePersonTotalsSuccessfully() {
        Person person1 = new Person(1L, "Mario Mario", 24);

        List<Transaction> transactions = List.of(
                new Transaction("Salary", BigDecimal.valueOf(5000), TransactionTypeEnum.RECEITA, person1),
                new Transaction("Freelance", BigDecimal.valueOf(2000), TransactionTypeEnum.RECEITA, person1),
                new Transaction("Rent", BigDecimal.valueOf(1500), TransactionTypeEnum.DESPESA, person1),
                new Transaction("Groceries", BigDecimal.valueOf(500), TransactionTypeEnum.DESPESA, person1)
        );

        when(transactionRepository.findByPersonId(person1.getId())).thenReturn(transactions);

        TotalTransactionDTO result = defaultTransactionService.getPersonTotals(person1.getId());

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(7000), result.totalIncome());
        assertEquals(BigDecimal.valueOf(2000), result.totalExpense());
        assertEquals(BigDecimal.valueOf(5000), result.balance());

        verify(transactionRepository).findByPersonId(person1.getId());
    }

    @Test
    void shouldReturnZeroWhenNoTransactionsFound() {
        Person person1 = new Person(1L, "Mario Mario", 24);

        when(transactionRepository.findByPersonId(person1.getId())).thenReturn(Collections.emptyList());

        TotalTransactionDTO result = defaultTransactionService.getPersonTotals(person1.getId());

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.totalIncome());
        assertEquals(BigDecimal.ZERO, result.totalExpense());
        assertEquals(BigDecimal.ZERO, result.balance());

        verify(transactionRepository).findByPersonId(person1.getId());
    }

    @Test
    void shouldCalculateTotalGeneralSuccessfully() {

        List<Transaction> transactions = List.of(
                new Transaction("Salary", BigDecimal.valueOf(5000), TransactionTypeEnum.RECEITA, person),
                new Transaction("Freelance", BigDecimal.valueOf(2000), TransactionTypeEnum.RECEITA, person),
                new Transaction("Rent", BigDecimal.valueOf(1500), TransactionTypeEnum.DESPESA, person),
                new Transaction("Groceries", BigDecimal.valueOf(500), TransactionTypeEnum.DESPESA, person)
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        TotalTransactionDTO result = defaultTransactionService.getTotalGeneral();

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(7000), result.totalIncome());
        assertEquals(BigDecimal.valueOf(2000), result.totalExpense());
        assertEquals(BigDecimal.valueOf(5000), result.balance());

        verify(transactionRepository).findAll();
    }

    @Test
    void shouldReturnZeroWhenNoTransactionsNotFound() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        TotalTransactionDTO result = defaultTransactionService.getTotalGeneral();

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.totalIncome());
        assertEquals(BigDecimal.ZERO, result.totalExpense());
        assertEquals(BigDecimal.ZERO, result.balance());

        verify(transactionRepository).findAll();
    }

    @Test
    void shouldReturnAllTransactionsSuccessfully() {
        List<Transaction> transactions = List.of(
                new Transaction("Salary", BigDecimal.valueOf(5000), TransactionTypeEnum.RECEITA, person),
                new Transaction("Freelance", BigDecimal.valueOf(2000), TransactionTypeEnum.RECEITA, person),
                new Transaction("Rent", BigDecimal.valueOf(1500), TransactionTypeEnum.DESPESA, person),
                new Transaction("Groceries", BigDecimal.valueOf(500), TransactionTypeEnum.DESPESA, person)
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<TransactionDTO> result = defaultTransactionService.getAllTransactions();


        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsFound() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<TransactionDTO> result = defaultTransactionService.getAllTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(transactionRepository).findAll();
        verify(transactionMapper, never()).toDTO(any(Transaction.class));
    }
}