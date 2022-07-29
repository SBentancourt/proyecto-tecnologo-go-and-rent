package com.tecnologo.grupo3.goandrent.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class FavoriteResponse {
    private int id;
    private String name;
    private String description;
    private String photo;
    private Float price;
    private Float qualification;
}
