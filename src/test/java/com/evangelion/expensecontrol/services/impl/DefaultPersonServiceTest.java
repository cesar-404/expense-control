package com.evangelion.expensecontrol.services.impl;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.dtos.transaction.TotalTransactionDTO;
import com.evangelion.expensecontrol.exceptions.EmptyPersonListException;
import com.evangelion.expensecontrol.exceptions.PersonNotFoundException;
import com.evangelion.expensecontrol.mappers.PersonMapper;
import com.evangelion.expensecontrol.models.person.Person;
import com.evangelion.expensecontrol.repositories.person.PersonRepository;
import com.evangelion.expensecontrol.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultPersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private DefaultPersonService defaultPersonService;

    private PersonDTO mockPersonDto;
    private PersonResponseDTO mockPersonResponseDTO;
    private Person mockPerson;

    @BeforeEach
    void setUp() {
        mockPersonDto = new PersonDTO("Test", 24);
        mockPersonResponseDTO = new PersonResponseDTO(1L, "Test", 24);
        mockPerson = new Person(1L, "Test", 24);
    }

    @Test
    void shouldCreatePersonSuccessfully() {

        when(personMapper.toEntity(mockPersonDto)).thenReturn(mockPerson);
        when(personRepository.save(mockPerson)).thenReturn(mockPerson);
        when(personMapper.toDto(mockPerson)).thenReturn(mockPersonResponseDTO);

        PersonResponseDTO createdPerson = defaultPersonService.createPerson(mockPersonDto);

        assertNotNull(createdPerson);
        assertEquals(mockPersonDto.name(), createdPerson.name());
        verify(personMapper, times(1)).toEntity(mockPersonDto);
        verify(personRepository, times(1)).save(mockPerson);
        verify(personMapper, times(1)).toDto(mockPerson);
    }

    @Test
    void shouldDeletePersonSuccessfully() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(mockPerson));

        defaultPersonService.deletePersonById(1L);

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldDeletePersonFailed() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(mockPerson));
        doThrow(new PersonNotFoundException("Person not found")).when(personRepository).deleteById(1L);

        Exception exception = assertThrows(PersonNotFoundException.class, () -> defaultPersonService.deletePersonById(1L));
        assertEquals("Person not found", exception.getMessage());

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnAListOfPersonsWithTotalsSuccessfully() {
        Person person1 = new Person(1L, "Mario Mario", 24);
        Person person2 = new Person(2L, "Luigi Mario", 22);
        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);
        TotalTransactionDTO dto1 = new TotalTransactionDTO(BigDecimal.valueOf(10),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(30));
        TotalTransactionDTO dto2 = new TotalTransactionDTO(BigDecimal.valueOf(20),
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(40));

        when(personRepository.findAll()).thenReturn(personList);
        when(transactionService.getPersonTotals(1L)).thenReturn(dto1);
        when(transactionService.getPersonTotals(2L)).thenReturn(dto2);





    }

    @Test
    void shouldReturnAListOfPersonsWithTotalsFailed() {
        List<Person> personList = new ArrayList<>();

        when(personRepository.findAll()).thenReturn(personList);

        assertThrows(EmptyPersonListException.class, () -> defaultPersonService.getPersonsWithTotals());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAListOfAllPersons() {
        Person person1 = new Person(1L, "Mario Mario", 24);
        Person person2 = new Person(2L, "Luigi Mario", 22);
        List<Person> personListTest = new ArrayList<>();
        personListTest.add(person1);
        personListTest.add(person2);

        when(personRepository.findAll()).thenReturn(personListTest);
        when(personMapper.toDto(person1)).thenReturn(new PersonResponseDTO(1L, "Mario Mario", 24));
        when(personMapper.toDto(person2)).thenReturn(new PersonResponseDTO(2L, "Luigi Mario", 22));

        List<PersonResponseDTO> result = defaultPersonService.getAllPersons();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Mario Mario", result.getFirst().name());
        assertEquals("Luigi Mario", result.get(1).name());
    }

    @Test
    void shouldReturnAListOfAllPersonsFailed() {
        ArrayList<Person> persons = new ArrayList<>();

        when(personRepository.findAll()).thenReturn(persons);

        assertThrows(EmptyPersonListException.class, () -> defaultPersonService.getAllPersons());
        verify(personRepository, times(1)).findAll();
    }
}