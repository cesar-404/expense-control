package com.evangelion.expensecontrol.repositories.transaction;

import com.evangelion.expensecontrol.models.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPersonId(Long personId);
}
