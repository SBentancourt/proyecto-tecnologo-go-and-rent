package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

@Data
public class HostSignUpBodyDTO {
    private String alias;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private String birthday;
    private String picture;
    private String bank;
    private String account;
    private AccommodationDTO accommodation;
}
