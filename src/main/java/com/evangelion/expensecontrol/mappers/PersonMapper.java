package com.evangelion.expensecontrol.mappers;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.models.person.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public Person toEntity(PersonDTO dto) {
        return new Person(
                dto.name(),
                dto.age());
    }

    public PersonResponseDTO toDto(Person entity) {
        return new PersonResponseDTO(entity.getId(),
                entity.getName(),
                entity.getAge());
    }
}
