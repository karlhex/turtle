package com.fwai.turtle.modules.customer.service;

import com.fwai.turtle.modules.customer.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PersonService {
    Page<PersonDTO> getPersons(Pageable pageable);
    List<PersonDTO> searchPersons(String query);
    PersonDTO getPersonById(Long id);
    PersonDTO createPerson(PersonDTO personDTO);
    PersonDTO updatePerson(Long id, PersonDTO personDTO);
    void deletePerson(Long id);
    List<PersonDTO> getAllPersons();
}
