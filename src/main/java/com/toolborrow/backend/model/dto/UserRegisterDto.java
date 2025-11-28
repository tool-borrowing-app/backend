package com.toolborrow.backend.model.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String postalCode;
    private String city;
    private String streetAddress;
    private String password;
}
