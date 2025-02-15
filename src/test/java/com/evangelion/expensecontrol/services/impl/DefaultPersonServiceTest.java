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
import com.evangelion.expensecontrol.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

        verify(personMapper).toEntity(mockPersonDto);
        verify(personRepository).save(mockPerson);
        verify(personMapper).toDto(mockPerson);
        verifyNoMoreInteractions(personMapper, personRepository);
    }

    @Test
    void shouldDeletePersonSuccessfully() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(mockPerson));

        defaultPersonService.deletePersonById(1L);

        verify(personRepository).findById(1L);
        verify(personRepository).deleteById(1L);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PersonNotFoundException.class, () ->
                defaultPersonService.deletePersonById(1L));

        assertEquals("Person not found", exception.getMessage());

        verify(personRepository).findById(1L);
        verify(personRepository, never()).deleteById(1L);
    }

    @Test
    void shouldReturnAListOfPersonsWithTotalsSuccessfully() {
        List<Person> personList = List.of(
                new Person(1L, "Mario Mario", 24),
                new Person(2L, "Luigi Mario", 22)
        );

        TotalTransactionDTO dto1 = new TotalTransactionDTO(BigDecimal.TEN, BigDecimal.valueOf(20), BigDecimal.valueOf(30));
        TotalTransactionDTO dto2 = new TotalTransactionDTO(BigDecimal.valueOf(20), BigDecimal.valueOf(30), BigDecimal.valueOf(40));

        when(personRepository.findAll()).thenReturn(personList);
        when(transactionService.getPersonTotals(1L)).thenReturn(dto1);
        when(transactionService.getPersonTotals(2L)).thenReturn(dto2);

        List<PersonWithTotalDTO> result = defaultPersonService.getPersonsWithTotals();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mario Mario", result.get(0).name());
        assertEquals(dto1, result.get(0).totals());
        assertEquals("Luigi Mario", result.get(1).name());
        assertEquals(dto2, result.get(1).totals());

        verify(personRepository).findAll();
        verify(transactionService).getPersonTotals(1L);
        verify(transactionService).getPersonTotals(2L);
        verifyNoMoreInteractions(personRepository, transactionService);
    }

    @Test
    void shouldThrowExceptionWhenPersonListIsEmpty() {
        when(personRepository.findAll()).thenReturn(List.of());

        assertThrows(EmptyPersonListException.class, () -> defaultPersonService.getPersonsWithTotals());

        verify(personRepository).findAll();
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void shouldReturnAListOfAllPersons() {
        List<Person> personList = List.of(
                new Person(1L, "Mario Mario", 24),
                new Person(2L, "Luigi Mario", 22)
        );

        when(personRepository.findAll()).thenReturn(personList);
        when(personMapper.toDto(any(Person.class)))
                .thenReturn(new PersonResponseDTO(1L, "Mario Mario", 24))
                .thenReturn(new PersonResponseDTO(2L, "Luigi Mario", 22));

        List<PersonResponseDTO> result = defaultPersonService.getAllPersons();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mario Mario", result.get(0).name());
        assertEquals("Luigi Mario", result.get(1).name());

        verify(personRepository).findAll();
        verify(personMapper, times(2)).toDto(any(Person.class));
        verifyNoMoreInteractions(personRepository, personMapper);
    }

    @Test
    void shouldThrowExceptionWhenGettingAllPersonsAndListIsEmpty() {
        when(personRepository.findAll()).thenReturn(List.of());

        assertThrows(EmptyPersonListException.class, () -> defaultPersonService.getAllPersons());

        verify(personRepository).findAll();
        verifyNoMoreInteractions(personRepository);
    }
}
