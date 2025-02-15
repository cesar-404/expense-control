package com.evangelion.expensecontrol.validator;

import com.evangelion.expensecontrol.exceptions.PersonIsNullException;
import com.evangelion.expensecontrol.exceptions.PersonNotFoundException;
import com.evangelion.expensecontrol.exceptions.PersonUderageException;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.models.transaction.TransactionTypeEnum;
import com.evangelion.expensecontrol.repositories.person.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionValidator {

    private static final Logger logger = LoggerFactory.getLogger(TransactionValidator.class);

    private final PersonRepository personRepository;

    public TransactionValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Método para validar a transação
    public void validateTransaction(Transaction transaction) {

        // Verifica se a pessoa não é nula
        var person = Optional.ofNullable(transaction.getPerson())
                .orElseThrow(() -> {
                    logger.error("Person is null in transaction validation");
                    return new PersonIsNullException("Person can not be null");
                });

        // Verifica se a pessoa existe no repositório
        var persistedPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> {
                    logger.error("Person with ID {} not found", person.getId());
                    return new PersonNotFoundException("Person not found");
                });

        // Verifica se a pessoa é menor de idade e tenta adicionar uma receita
        if (persistedPerson.getAge() < 18 && transaction.getType() == TransactionTypeEnum.RECEITA) {
            logger.error("Person under 18 tried to add income: Person ID {}, Age {}", person.getId(), persistedPerson.getAge());
            throw new PersonUderageException("You are not allowed to add income for individuals under 18 years old.");
        }

        logger.info("Transaction validated successfully for person ID: {}", person.getId());
    }
}
