package com.evangelion.expensecontrol.dtos.person;

import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;

public record PersonWithTotalDTO(String name,
                                 TotalTransactionDTO totals) {
}