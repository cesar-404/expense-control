package com.evangelion.expensecontrol.validator;

import com.evangelion.expensecontrol.exceptions.PersonIsNullException;
import com.evangelion.expensecontrol.exceptions.PersonNotFoundException;
import com.evangelion.expensecontrol.exceptions.PersonUderageException;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.models.transaction.TransactionTypeEnum;
import com.evangelion.expensecontrol.repositories.person.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionValidator {

    private final PersonRepository personRepository;

    public TransactionValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void validateTransaction(Transaction transaction) {

        var person = Optional.ofNullable(transaction.getPerson())
                .orElseThrow(() -> new PersonIsNullException("Person can not be null"));

        var persistedPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));

        if (persistedPerson.getAge() < 18 && transaction.getType() == TransactionTypeEnum.RECEITA) {
            throw new PersonUderageException("You are not allowed to add income for individuals under 18 years old.");
        }

    }
}