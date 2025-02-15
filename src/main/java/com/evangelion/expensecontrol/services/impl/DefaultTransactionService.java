package com.evangelion.expensecontrol.services.impl;

import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.mappers.TransactionMapper;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.models.transaction.TransactionTypeEnum;
import com.evangelion.expensecontrol.repositories.transaction.TransactionRepository;
import com.evangelion.expensecontrol.services.TransactionService;
import com.evangelion.expensecontrol.validator.TransactionValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DefaultTransactionService implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionValidator transactionValidator;

    public DefaultTransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.transactionValidator = transactionValidator;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        var transaction = transactionMapper.toEntity(transactionDTO);
        transactionValidator.validateTransaction(transaction);
        var savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDTO(savedTransaction);
    }

    @Override
    public TotalTransactionDTO getPersonTotals(Long personId) {
        BigDecimal totalIncome = transactionRepository.findByPersonId(personId)
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.RECEITA)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactionRepository.findByPersonId(personId)
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.DESPESA)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new TotalTransactionDTO(totalIncome, balance, totalExpense);
    }

    @Override
    public TotalTransactionDTO getTotalGeneral() {
        BigDecimal totalIncome = transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.RECEITA)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactionRepository.findAll()
                .stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.DESPESA)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new TotalTransactionDTO(totalIncome, balance, totalExpense);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDTO)
                .toList();
    }
}