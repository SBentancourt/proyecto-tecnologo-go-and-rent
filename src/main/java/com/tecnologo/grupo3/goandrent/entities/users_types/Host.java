package com.tecnologo.grupo3.goandrent.entities.users_types;

import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.utils.enums.Bank;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("ROLE_HOST")
@Data
public class Host extends User {
    private Bank bank;
    private String account;

    public Host(String alias, String email, String password, String name, String lastName, UserStatus userStatus, Date createdAt, String phone, Date birthday, String picture, Bank bank, String account) {
        super(alias, email, password, name, lastName, userStatus, createdAt, phone, birthday, picture);
        this.bank = bank;
        this.account = account;
    }

    public Host(Bank bank, String account) {
        this.bank = bank;
        this.account = account;
    }

    public Host() {
    }
}
