package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ReviewInfoDTO {
    private int qualification;
    private String description;
    private String guestName;
    private String guestImage;
}
