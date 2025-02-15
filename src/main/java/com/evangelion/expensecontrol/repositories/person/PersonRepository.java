package com.evangelion.expensecontrol.repositories.person;

import com.evangelion.expensecontrol.models.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
