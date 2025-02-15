package com.evangelion.expensecontrol.services;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.dtos.person.PersonWithTotalDTO;

import java.util.List;

public interface PersonService {

    PersonResponseDTO createPerson(PersonDTO personDTO);

    void deletePersonById(Long id);

    List<PersonWithTotalDTO> getPersonsWithTotals();

    List<PersonResponseDTO> getAllPersons();


}
