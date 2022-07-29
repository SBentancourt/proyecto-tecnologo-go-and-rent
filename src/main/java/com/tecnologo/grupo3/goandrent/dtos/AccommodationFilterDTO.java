package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccommodationFilterDTO {
    private String country;
    private String province;
    private String city;
    private String dateFrom;
    private String dateTo;
    private String priceFrom;
    private String priceTo;
    private List<String> services;
    private List<String> features;
}
