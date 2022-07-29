package com.tecnologo.grupo3.goandrent.entities.users_types;


import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@DiscriminatorValue("ROLE_GUEST")
public class Guest extends User {

    public Guest(String alias, String email, String password, String name, String lastName, UserStatus userStatus, Date createdAt, String phone, Date birthday, String picture) {
        super(alias, email, password, name, lastName, userStatus, createdAt, phone, birthday, picture);
    }

    public Guest() {
    }
}
