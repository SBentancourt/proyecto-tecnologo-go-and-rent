package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HostConfirmedRefusedDTO {
    private int total;
    private int status;
    private int month;
    private int year;
}
