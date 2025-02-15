package com.evangelion.expensecontrol.dtos.transaction;

import com.evangelion.expensecontrol.models.transaction.TransactionTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(@NotBlank(message = "The field cannot be empty")
                             String description,
                             @NotNull(message = "The field cannot be empty")
                             BigDecimal amount,
                             @NotNull(message = "The field cannot be empty")
                             TransactionTypeEnum type,
                             @NotNull(message = "The field cannot be empty")
                             Long personId) {
}
