package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UserInformationDTO {
    private String alias;
    private String email;
    private String name;
    private String lastName;
    private String creationDate;
    private String status;
    private String phone;
    private String role;
    private Float rating;
    private int accommodationId;

    public UserInformationDTO(String alias, String email, String name, String lastName, String creationDate, String status, String phone, String role) {
        this.alias = alias;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.creationDate = creationDate;
        this.status = status;
        this.phone = phone;
        this.role = role;
    }
}
