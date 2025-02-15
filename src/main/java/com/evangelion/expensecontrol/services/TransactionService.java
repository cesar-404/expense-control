package com.evangelion.expensecontrol.services;

import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    List<TransactionDTO> getAllTransactions();

    TotalTransactionDTO getPersonTotals(Long personId);

    TotalTransactionDTO getTotalGeneral();

}
