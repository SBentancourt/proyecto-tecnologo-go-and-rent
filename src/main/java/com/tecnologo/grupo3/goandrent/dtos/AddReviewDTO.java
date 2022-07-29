package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

@Data
public class AddReviewDTO {
    private int bookingId;
    private String description;
    private int qualification;
}
