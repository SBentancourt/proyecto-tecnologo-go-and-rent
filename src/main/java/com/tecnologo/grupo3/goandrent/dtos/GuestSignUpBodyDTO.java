package com.tecnologo.grupo3.goandrent.dtos;

import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class GuestSignUpBodyDTO {
    private String alias;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private String birthday;
    private String picture;
}
