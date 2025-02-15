package com.evangelion.expensecontrol.mappers;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.models.person.Person;
import org.springframework.stereotype.Component;

@Component // Indica que essa classe é um componente do Spring e será gerenciada pelo contexto de aplicação
public class PersonMapper {

    // Converte um DTO de pessoa para uma entidade de pessoa
    public Person toEntity(PersonDTO dto) {
        return new Person(
                dto.name(),
                dto.age());
    }

    // Converte uma entidade de pessoa para um DTO de resposta de pessoa
    public PersonResponseDTO toDto(Person entity) {
        return new PersonResponseDTO(entity.getId(),
                entity.getName(),
                entity.getAge());
    }
}
