package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class InfoProfileDTO {
    private String alias;
    private String email;
    private String name;
    private String lastName;
    private String phone;
    private String birthday;
    private String picture;
    private String bank;
    private String account;
    private Float qualification;

    public InfoProfileDTO(String alias, String email, String name, String lastName, String phone, String birthday, String picture) {
        this.alias = alias;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.picture = picture;
    }
}
