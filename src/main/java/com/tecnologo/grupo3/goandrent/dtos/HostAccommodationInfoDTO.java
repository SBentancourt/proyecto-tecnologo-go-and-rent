package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class HostAccommodationInfoDTO {
    private int accommodationId;
    private String name;
    private String description;
    private Boolean reservationInProgress;
    private String status;
    private String photo;
}
