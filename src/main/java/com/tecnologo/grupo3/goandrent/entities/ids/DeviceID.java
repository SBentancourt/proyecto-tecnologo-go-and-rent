package com.tecnologo.grupo3.goandrent.entities.ids;

import com.tecnologo.grupo3.goandrent.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor
@Embeddable
public class DeviceID implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_alias", referencedColumnName = "alias", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_Device_User_alias"))
    private User userAlias;

    @Column(name = "device_id", nullable = false)
    private String deviceId;
}
