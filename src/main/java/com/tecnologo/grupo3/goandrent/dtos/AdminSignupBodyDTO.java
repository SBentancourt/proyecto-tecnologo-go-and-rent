package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AdminSignupBodyDTO {
    private String alias;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private String birthday;
    private String picture;
}
