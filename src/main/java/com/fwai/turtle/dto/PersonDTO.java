package com.fwai.turtle.dto;

import lombok.Data;

@Data
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
}
