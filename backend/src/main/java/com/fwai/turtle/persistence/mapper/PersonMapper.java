package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.persistence.entity.Person;
import com.fwai.turtle.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {
    
    PersonDTO toDTO(Person person);
    
    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(PersonDTO source, @MappingTarget Person target);
    
    @Mapping(target = "id", ignore = true)
    Person toPerson(Person person);
    
    @Mapping(target = "id", ignore = true)
    void updatePerson(Person source, @MappingTarget Person target);
}
