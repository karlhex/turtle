package com.fwai.turtle.persistence.converter;

import com.fwai.turtle.types.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        for (RoleType role : RoleType.values()) {
            if (role.getValue().equals(dbData)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
