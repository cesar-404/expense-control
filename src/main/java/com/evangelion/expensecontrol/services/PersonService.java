package com.evangelion.expensecontrol.services;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.dtos.person.PersonWithTotalDTO;

import java.util.List;

public interface PersonService {

    // Cria uma nova pessoa a partir de um DTO e retorna os dados da pessoa criada
    PersonResponseDTO createPerson(PersonDTO personDTO);

    // Deleta uma pessoa pelo ID
    void deletePersonById(Long id);

    // Obtém uma lista de pessoas com seus totais de transações
    List<PersonWithTotalDTO> getPersonsWithTotals();

    // Obtém uma lista de todas as pessoas cadastradas
    List<PersonResponseDTO> getAllPersons();

}
