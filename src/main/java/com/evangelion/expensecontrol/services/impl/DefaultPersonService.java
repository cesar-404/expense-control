package com.evangelion.expensecontrol.services.impl;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.dtos.person.PersonWithTotalDTO;
import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.exceptions.EmptyPersonListException;
import com.evangelion.expensecontrol.exceptions.PersonNotFoundException;
import com.evangelion.expensecontrol.mappers.PersonMapper;
import com.evangelion.expensecontrol.models.person.Person;
import com.evangelion.expensecontrol.repositories.person.PersonRepository;
import com.evangelion.expensecontrol.services.PersonService;
import com.evangelion.expensecontrol.services.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPersonService implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final TransactionService transactionService;

    public DefaultPersonService(PersonRepository personRepository, PersonMapper personMapper, TransactionService transactionService) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.transactionService = transactionService;
    }


    @Override
    public PersonResponseDTO createPerson(PersonDTO personDTO) {
        var person = personMapper.toEntity(personDTO);
        personRepository.save(person);
        return personMapper.toDto(person);
    }

    @Override
    public void deletePersonById(Long id) {
        personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        personRepository.deleteById(id);
    }

    @Override
    public List<PersonWithTotalDTO> getPersonsWithTotals() {
        List<Person> persons = personRepository.findAll();

        if (persons.isEmpty()) {
            throw new EmptyPersonListException("No persons found in the system");
        }

        return persons.stream()
                .map(person -> {
                    TotalTransactionDTO totals = transactionService.getPersonTotals(person.getId());
                    return new PersonWithTotalDTO(
                            person.getName(),
                            totals
                    );
                })
                .toList();
    }

    @Override
    public List<PersonResponseDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();

        if (persons.isEmpty()) {
            throw new EmptyPersonListException("No persons available to display");
        }

        return persons.stream()
                .map(personMapper::toDto)
                .toList();
    }
}