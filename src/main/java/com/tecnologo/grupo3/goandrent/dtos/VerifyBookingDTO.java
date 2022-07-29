package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

@Data
public class VerifyBookingDTO {
    private int id_accommodation;
    private String start_date;
    private String end_date;
}
