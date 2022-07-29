package com.tecnologo.grupo3.goandrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iGallery02", columnList = "accommodation_id")})
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String photo;
    private int orderPhoto;

    public Gallery(String name, String photo, int orderPhoto) {
        this.name = name;
        this.photo = photo;
        this.orderPhoto = orderPhoto;
    }
}
