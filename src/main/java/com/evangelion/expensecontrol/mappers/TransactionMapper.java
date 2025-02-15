package com.evangelion.expensecontrol.mappers;

import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.models.person.Person;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.repositories.person.PersonRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final PersonRepository personRepository;

    public TransactionMapper(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Transaction toEntity(TransactionDTO dto) {

        Person person = personRepository.findById(dto.personId())
                .orElseThrow(() -> new RuntimeException("Person not found with ID " + dto.personId()));

        return new Transaction(dto.description(),
                dto.amount(),
                dto.type(),
                person);
    }

    public TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getPerson() != null ? transaction.getPerson().getId() : null);
    }

}
