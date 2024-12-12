package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.PersonDTO;
import com.fwai.turtle.persistence.entity.Person;
import com.fwai.turtle.persistence.repository.PersonRepository;
import com.fwai.turtle.service.interfaces.PersonService;
import com.fwai.turtle.persistence.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public Page<PersonDTO> getPersons(Pageable pageable) {
        return personRepository.findAll(pageable)
                .map(personMapper::toDTO);
    }

    @Override
    public List<PersonDTO> searchPersons(String query) {
        return personRepository.searchByNameOrPhone(query).stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO getPersonById(Long id) {
        return personRepository.findById(id)
                .map(personMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = personMapper.toEntity(personDTO);
        person = personRepository.save(person);
        return personMapper.toDTO(person);
    }

    @Override
    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        
        personMapper.updateEntityFromDTO(personDTO, existingPerson);
        existingPerson = personRepository.save(existingPerson);
        return personMapper.toDTO(existingPerson);
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }
}
