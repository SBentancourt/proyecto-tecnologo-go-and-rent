package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class FeatureValueAccDTO {
    private int id;
    private String name;
    private int value;
}
