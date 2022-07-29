package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class HostInfoDTO {
    private String alias;
    private String name;
    private String photo;
    private Float qualification;
}
