package com.tecnologo.grupo3.goandrent.entities.ids;

import com.tecnologo.grupo3.goandrent.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor
@Embeddable
public class UserCalificationID implements Serializable {
    @Column(name = "qualifying_user", nullable = false)
    private String qualifyingUser;

    @ManyToOne
    @JoinColumn(name = "qualified_user", referencedColumnName = "alias", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_UserCalification_User_alias_02"))
    private User qualifiedUser;

}
