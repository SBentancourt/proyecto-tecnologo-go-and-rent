package com.tecnologo.grupo3.goandrent.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecnologo.grupo3.goandrent.entities.ids.UserCalificationID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iUserCalification02", columnList = "qualifying_user")})
public class UserCalification implements Serializable {
    @EmbeddedId @JsonProperty("id")
    private UserCalificationID userCalificationID;

    private int qualification;

    @ManyToOne
    @MapsId("qualifyingUser")
    @JoinColumn(name = "qualifying_user", referencedColumnName = "alias", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_UserCalification_User_alias_01"))
    private User user;

}
