package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.PersonDTO;
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
