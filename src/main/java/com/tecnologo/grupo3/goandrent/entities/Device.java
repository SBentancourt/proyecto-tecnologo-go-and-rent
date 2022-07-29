package com.tecnologo.grupo3.goandrent.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecnologo.grupo3.goandrent.entities.ids.DeviceID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iDevice02", columnList = "user_alias")})
public class Device implements Serializable {

    @EmbeddedId
    @JsonProperty("id")
    private DeviceID deviceID;
}
