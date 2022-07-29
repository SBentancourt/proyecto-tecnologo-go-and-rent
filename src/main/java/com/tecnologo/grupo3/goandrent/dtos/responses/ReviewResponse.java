package com.tecnologo.grupo3.goandrent.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReviewResponse {
    private String guestAlias;
    private String guestName;
    private String guestPhoto;
    private int qualification;
    private String description;
    private String created_at;
}
