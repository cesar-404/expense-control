package com.evangelion.expensecontrol.dtos.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonDTO(@NotBlank(message = "The field cannot be empty")
                        String name,
                        @NotNull(message = "The field cannot be empty")
                        Integer age) {
}
