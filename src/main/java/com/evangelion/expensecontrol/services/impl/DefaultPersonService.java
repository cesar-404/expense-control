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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultPersonService implements PersonService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPersonService.class);

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final TransactionService transactionService;

    // Construtor que inicializa as dependências
    public DefaultPersonService(PersonRepository personRepository, PersonMapper personMapper, TransactionService transactionService) {
        this.personRepository = personRepository; // Inicializa o repositório de pessoas
        this.personMapper = personMapper; // Inicializa o mapper para converter entre objetos DTO e entidade
        this.transactionService = transactionService; // Inicializa o serviço para transações
    }

    // Cria uma nova pessoa a partir do DTO e salva no repositório
    @Override
    public PersonResponseDTO createPerson(PersonDTO personDTO) {
        logger.info("Starting creation of person: {}", personDTO.name());
        var person = personMapper.toEntity(personDTO); // Converte o DTO para a entidade Person
        personRepository.save(person); // Salva a entidade no repositório
        logger.info("Person successfully created with ID: {}", person.getId());
        return personMapper.toDto(person); // Retorna o DTO da pessoa criada
    }

    // Exclui uma pessoa pelo ID, caso exista
    @Override
    public void deletePersonById(Long id) {
        logger.info("Attempting to delete person with ID: {}", id);
        personRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Person with ID {} not found", id);
                    return new PersonNotFoundException("Pessoa não encontrada");
                });
        personRepository.deleteById(id); // Exclui a pessoa pelo ID
        logger.info("Person with ID {} successfully deleted", id);
    }

    // Retorna todas as pessoas com os totais das transações
    @Override
    public List<PersonWithTotalDTO> getPersonsWithTotals() {
        logger.info("Retrieving all persons with transaction totals");
        List<Person> persons = personRepository.findAll(); // Recupera todas as pessoas do repositório

        // Se não houver pessoas, lança uma exceção
        if (persons.isEmpty()) {
            logger.error("No persons found");
            throw new EmptyPersonListException("Nenhuma pessoa encontrada");
        }

        // Mapeia cada pessoa para um DTO contendo o nome e os totais das transações
        return persons.stream()
                .map(person -> {
                    TotalTransactionDTO totals = transactionService.getPersonTotals(person.getId()); // Obtém os totais das transações da pessoa
                    return new PersonWithTotalDTO(person.getName(), totals); // Retorna o DTO com o nome e os totais
                })
                .collect(Collectors.toList()); // Coleta e retorna a lista de PersonWithTotalDTO
    }

    // Retorna todas as pessoas cadastradas
    @Override
    public List<PersonResponseDTO> getAllPersons() {
        logger.info("Retrieving all persons");
        List<Person> persons = personRepository.findAll(); // Recupera todas as pessoas do repositório

        // Se não houver pessoas, lança uma exceção
        if (persons.isEmpty()) {
            logger.error("No persons found");
            throw new EmptyPersonListException("Nenhuma pessoa encontrada");
        }

        // Converte cada entidade Person para o DTO correspondente
        return persons.stream()
                .map(personMapper::toDto) // Converte a entidade Person para o DTO PersonResponseDTO
                .collect(Collectors.toList()); // Coleta e retorna a lista de PersonResponseDTO
    }
}