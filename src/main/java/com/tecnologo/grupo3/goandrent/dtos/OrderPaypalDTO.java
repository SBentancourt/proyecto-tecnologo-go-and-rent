package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class OrderPaypalDTO {
    private Double price;
    private String currency;
    private String method;
    private String intent;
    private String description;
}
