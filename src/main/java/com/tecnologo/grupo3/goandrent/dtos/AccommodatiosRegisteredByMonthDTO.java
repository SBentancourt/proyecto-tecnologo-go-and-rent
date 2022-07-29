package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AccommodatiosRegisteredByMonthDTO {
    private int total;
    private int month;
    private int year;
}
