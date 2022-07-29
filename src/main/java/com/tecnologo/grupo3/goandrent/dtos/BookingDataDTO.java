package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

@Data
public class BookingDataDTO {
    private int idAccommodation;
    private String aliasGuest;
    private String start_date;
    private String end_date;
}
