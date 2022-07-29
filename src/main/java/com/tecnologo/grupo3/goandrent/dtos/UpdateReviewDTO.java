package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UpdateReviewDTO {
    private int reviewId;
    private int qualification;
    private String description;
}
