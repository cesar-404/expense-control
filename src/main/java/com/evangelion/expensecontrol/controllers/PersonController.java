package com.evangelion.expensecontrol.controllers;

import com.evangelion.expensecontrol.dtos.person.PersonDTO;
import com.evangelion.expensecontrol.dtos.person.PersonResponseDTO;
import com.evangelion.expensecontrol.services.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people") // Define o caminho base para as requisições relacionadas às pessoas
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class); // Logger para registrar eventos e informações
    private final PersonService personService; // Serviço para gerenciar informações de pessoas

    public PersonController(PersonService personService) {
        this.personService = personService; // Injeção do serviço de pessoas
    }

    @PostMapping // Mapeia a requisição POST para a criação de uma nova pessoa
    public ResponseEntity<PersonResponseDTO> create(@RequestBody @Valid PersonDTO personDTO) {
        logger.info("Creating new person with data: {}", personDTO);
        var response = personService.createPerson(personDTO); // Chama o método para criação de pessoa da classe de serviço
        logger.info("Person created successfully with ID: {}", response.id());
        return ResponseEntity.ok().body(response); // Retorna os dados da pessoa cadastrada com o id
    }

    @DeleteMapping("/{id}") // Mapeia a requisição DELETE para deletar uma pessoa
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting person with ID: {}", id);
        personService.deletePersonById(id); // Chama o método da classe de serviço para deletar uma pessoa por id
        logger.info("Person with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build(); // Retorna uma resposta sem conteúdo após a exclusão
    }

    @GetMapping // Mapeia a requisição GET para listar todas as pessoas cadastradas
    public ResponseEntity<List<PersonResponseDTO>> getAll() {
        logger.info("Fetching all persons");
        List<PersonResponseDTO> persons = personService.getAllPersons(); // Chama o método da classe de serviço para listar todas as pessoas cadastradas
        logger.info("Retrieved {} persons", persons.size());
        return ResponseEntity.ok().body(persons); // Retorna no corpo uma lista com todas as pessoas cadastradas
    }
}