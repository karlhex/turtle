package com.fwai.turtle.modules.customer.dto;

import lombok.Data;

@Data
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String mobilePhone;
    private String workPhone;
    private String homePhone;
    private String email;
    private String address;
    private String companyName;
    private String department;
    private String position;
}
