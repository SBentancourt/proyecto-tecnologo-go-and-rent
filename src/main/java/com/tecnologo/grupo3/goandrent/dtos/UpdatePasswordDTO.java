package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String alias;
    private String oldPassword;
    private String newPassword;
}
