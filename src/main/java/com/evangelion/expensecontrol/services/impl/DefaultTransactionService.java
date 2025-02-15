package com.evangelion.expensecontrol.services.impl;

import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.mappers.TransactionMapper;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.models.transaction.TransactionTypeEnum;
import com.evangelion.expensecontrol.repositories.transaction.TransactionRepository;
import com.evangelion.expensecontrol.services.TransactionService;
import com.evangelion.expensecontrol.validator.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultTransactionService implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTransactionService.class);

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionValidator transactionValidator;

    // Construtor da classe que injeta as dependências do repositório, mapper e validador
    public DefaultTransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper,
                                     TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.transactionValidator = transactionValidator;
    }

    // Método para criar uma nova transação
    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        logger.info("Starting transaction creation for user: {}", transactionDTO.personId());

        // Converte o DTO em entidade para persistir no banco
        var transaction = transactionMapper.toEntity(transactionDTO);

        logger.debug("Validating transaction: {}", transactionDTO);

        // Valida a transação antes de salvar
        transactionValidator.validateTransaction(transaction);

        logger.debug("Saving transaction to repository...");

        // Salva a transação no repositório
        var savedTransaction = transactionRepository.save(transaction);

        logger.info("Transaction created successfully: {}", savedTransaction.getId());

        // Retorna a transação salva como DTO
        return transactionMapper.toDTO(savedTransaction);
    }

    // Método para obter o total de receitas, despesas e saldo de uma pessoa específica
    @Override
    public TotalTransactionDTO getPersonTotals(Long personId) {
        logger.info("Retrieving totals for person with ID: {}", personId);

        // Recupera as transações da pessoa pelo ID
        List<Transaction> transactions = transactionRepository.findByPersonId(personId);

        // Calcula a receita total da pessoa
        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.RECEITA)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcula a despesa total da pessoa
        BigDecimal totalExpense = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionTypeEnum.DESPESA)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcula o saldo da pessoa
        BigDecimal balance = totalIncome.subtract(totalExpense);

        logger.info("Total income: {}, Total expense: {}, Balance: {}", totalIncome, totalExpense, balance);

        // Retorna os totais e o saldo em um DTO
        return new TotalTransactionDTO(totalIncome, totalExpense, balance);
    }

    // Método para obter os totais gerais de todas as transações
    @Override
    public TotalTransactionDTO getTotalGeneral() {
        logger.info("Retrieving general totals...");

        // Particiona as transações entre receitas e despesas
        Map<Boolean, List<Transaction>> partitionedTransactions = transactionRepository.findAll().stream()
                .collect(Collectors.partitioningBy(transaction -> transaction.getType() == TransactionTypeEnum.RECEITA));

        // Calcula a receita total de todas as transações
        BigDecimal totalIncome = partitionedTransactions.get(true).stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcula a despesa total de todas as transações
        BigDecimal totalExpense = partitionedTransactions.get(false).stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcula o saldo geral
        BigDecimal balance = totalIncome.subtract(totalExpense);

        logger.info("Total income: {}, Total expense: {}, Balance: {}", totalIncome, totalExpense, balance);

        // Retorna os totais gerais e o saldo em um DTO
        return new TotalTransactionDTO(totalIncome, totalExpense, balance);
    }

    // Método para obter todas as transações
    @Override
    public List<TransactionDTO> getAllTransactions() {
        logger.info("Retrieving all transactions...");

        // Recupera todas as transações e as converte em DTOs
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDTO)
                .toList();
    }
}
