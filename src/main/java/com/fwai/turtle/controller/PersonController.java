package com.fwai.turtle.controller;

import com.fwai.turtle.dto.PersonDTO;
import com.fwai.turtle.service.interfaces.PersonService;
import com.fwai.turtle.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PersonDTO>>> getPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PersonDTO> personPage = personService.getPersons(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(personPage));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PersonDTO>>> searchPersons(
            @RequestParam String query) {
        List<PersonDTO> persons = personService.searchPersons(query);
        return ResponseEntity.ok(ApiResponse.ok(persons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonDTO>> getPersonById(@PathVariable Long id) {
        PersonDTO person = personService.getPersonById(id);
        return ResponseEntity.ok(ApiResponse.ok(person));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PersonDTO>> createPerson(@RequestBody PersonDTO personDTO) {
        PersonDTO createdPerson = personService.createPerson(personDTO);
        return ResponseEntity.ok(ApiResponse.ok(createdPerson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonDTO>> updatePerson(
            @PathVariable Long id,
            @RequestBody PersonDTO personDTO) {
        PersonDTO updatedPerson = personService.updatePerson(id, personDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedPerson));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PersonDTO>>> getAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();
        return ResponseEntity.ok(ApiResponse.ok(persons));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
