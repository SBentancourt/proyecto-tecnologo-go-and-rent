package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RegisteredUserPerMonthDTO {
    private int total;
    private String role;
    private int month;
    private int year;
}
