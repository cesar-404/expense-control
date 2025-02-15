package com.evangelion.expensecontrol.services;

import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;

import java.util.List;

public interface TransactionService {

    // Cria uma nova transação a partir de um DTO e retorna os dados da transação criada
    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    // Obtém uma lista de todas as transações cadastradas
    List<TransactionDTO> getAllTransactions();

    // Obtém o total de transações de uma pessoa específica
    TotalTransactionDTO getPersonTotals(Long personId);

    // Obtém o total geral de transações
    TotalTransactionDTO getTotalGeneral();

}
