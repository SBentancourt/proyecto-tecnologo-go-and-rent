package com.tecnologo.grupo3.goandrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String country;
    private String province;
    private String city;
    private String street;
    private int doorNumber;
    private String coordinates;

    public Location(String country, String province, String city, String street, int doorNumber, String coordinates) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.street = street;
        this.doorNumber = doorNumber;
        this.coordinates = coordinates;
    }
}
