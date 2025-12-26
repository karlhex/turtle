package com.fwai.turtle.modules.organization.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwai.turtle.base.types.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeApplicationDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testStatusDeserializationWithOldValues() throws Exception {
        // Test old status value "SUBMITTED" maps to PENDING
        String jsonWithSubmitted = "{\"status\":\"SUBMITTED\",\"name\":\"Test User\"}";
        EmployeeApplicationDTO dto = objectMapper.readValue(jsonWithSubmitted, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.PENDING, dto.getStatus());

        // Test old status value "SUPPLEMENTARY_REQUIRED" maps to UNDER_REVIEW
        String jsonWithSupplementary = "{\"status\":\"SUPPLEMENTARY_REQUIRED\",\"name\":\"Test User\"}";
        dto = objectMapper.readValue(jsonWithSupplementary, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.UNDER_REVIEW, dto.getStatus());
    }

    @Test
    public void testStatusDeserializationWithNewValues() throws Exception {
        // Test new status values work correctly
        String jsonWithPending = "{\"status\":\"PENDING\",\"name\":\"Test User\"}";
        EmployeeApplicationDTO dto = objectMapper.readValue(jsonWithPending, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.PENDING, dto.getStatus());

        String jsonWithValidated = "{\"status\":\"VALIDATED\",\"name\":\"Test User\"}";
        dto = objectMapper.readValue(jsonWithValidated, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.VALIDATED, dto.getStatus());

        String jsonWithApproved = "{\"status\":\"APPROVED\",\"name\":\"Test User\"}";
        dto = objectMapper.readValue(jsonWithApproved, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.APPROVED, dto.getStatus());

        String jsonWithRejected = "{\"status\":\"REJECTED\",\"name\":\"Test User\"}";
        dto = objectMapper.readValue(jsonWithRejected, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.REJECTED, dto.getStatus());

        String jsonWithUnderReview = "{\"status\":\"UNDER_REVIEW\",\"name\":\"Test User\"}";
        dto = objectMapper.readValue(jsonWithUnderReview, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.UNDER_REVIEW, dto.getStatus());
    }

    @Test
    public void testStatusDeserializationWithInvalidValue() throws Exception {
        // Test invalid status value defaults to PENDING
        String jsonWithInvalid = "{\"status\":\"INVALID_STATUS\",\"name\":\"Test User\"}";
        EmployeeApplicationDTO dto = objectMapper.readValue(jsonWithInvalid, EmployeeApplicationDTO.class);
        assertEquals(ApplicationStatus.PENDING, dto.getStatus());
    }

    @Test
    public void testStatusDeserializationWithNullValue() throws Exception {
        // Test null status value
        String jsonWithNull = "{\"status\":null,\"name\":\"Test User\"}";
        EmployeeApplicationDTO dto = objectMapper.readValue(jsonWithNull, EmployeeApplicationDTO.class);
        assertNull(dto.getStatus());
    }

    @Test
    public void testStatusDeserializationWithEmptyValue() throws Exception {
        // Test empty status value
        String jsonWithEmpty = "{\"status\":\"\",\"name\":\"Test User\"}";
        EmployeeApplicationDTO dto = objectMapper.readValue(jsonWithEmpty, EmployeeApplicationDTO.class);
        assertNull(dto.getStatus());
    }
}