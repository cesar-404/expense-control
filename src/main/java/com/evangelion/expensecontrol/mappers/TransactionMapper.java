package com.evangelion.expensecontrol.mappers;

import com.evangelion.expensecontrol.dtos.transaction.TransactionDTO;
import com.evangelion.expensecontrol.exceptions.PersonNotFoundException;
import com.evangelion.expensecontrol.models.person.Person;
import com.evangelion.expensecontrol.models.transaction.Transaction;
import com.evangelion.expensecontrol.repositories.person.PersonRepository;
import org.springframework.stereotype.Component;

@Component // Indica que a classe é um componente do Spring e será gerenciada pelo contexto de aplicação
public class TransactionMapper {

    private final PersonRepository personRepository; // Repositório de pessoas, usado para buscar uma pessoa pelo ID

    public TransactionMapper(PersonRepository personRepository) {
        this.personRepository = personRepository; // Injeção do repositório de pessoas
    }

    // Converte um DTO de transação para uma entidade de transação
    public Transaction toEntity(TransactionDTO dto) {

        // Busca a pessoa associada à transação pelo ID. Se não for encontrada, lança uma exceção
        Person person = personRepository.findById(dto.personId())
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));

        // Cria e retorna uma nova entidade de transação
        return new Transaction(dto.description(),
                dto.amount(),
                dto.type(),
                person);
    }

    // Converte uma entidade de transação para um DTO de transação
    public TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getPerson() != null ? transaction.getPerson().getId() : null);
    }

}
