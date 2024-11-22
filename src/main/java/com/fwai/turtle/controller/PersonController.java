package com.fwai.turtle.controller;

import com.fwai.turtle.dto.PersonDTO;
import com.fwai.turtle.common.PageResponse;
import com.fwai.turtle.service.interfaces.PersonService;
import com.fwai.turtle.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<Result<Page<PersonDTO>>> getPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PersonDTO> personPage = personService.getPersons(PageRequest.of(page, size));
        return ResponseEntity.ok(Result.success(personPage));
    }

    @GetMapping("/search")
    public ResponseEntity<Result<List<PersonDTO>>> searchPersons(
            @RequestParam String query) {
        List<PersonDTO> persons = personService.searchPersons(query);
        return ResponseEntity.ok(Result.success(persons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<PersonDTO>> getPersonById(@PathVariable Long id) {
        PersonDTO person = personService.getPersonById(id);
        return ResponseEntity.ok(Result.success(person));
    }

    @PostMapping
    public ResponseEntity<Result<PersonDTO>> createPerson(@RequestBody PersonDTO personDTO) {
        PersonDTO createdPerson = personService.createPerson(personDTO);
        return ResponseEntity.ok(Result.success(createdPerson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<PersonDTO>> updatePerson(
            @PathVariable Long id,
            @RequestBody PersonDTO personDTO) {
        PersonDTO updatedPerson = personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(Result.success(updatedPerson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok(Result.success(null));
    }
}
