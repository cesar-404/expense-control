package com.evangelion.expensecontrol.dtos.transaction;

import java.math.BigDecimal;

public record TotalTransactionDTO(BigDecimal totalIncome,
                                  BigDecimal totalExpense,
                                  BigDecimal balance) {
}
