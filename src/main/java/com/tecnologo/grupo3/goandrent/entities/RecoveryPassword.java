package com.tecnologo.grupo3.goandrent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iRecoveryPassword02", columnList = "user_alias")})
public class RecoveryPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int code;
    private Date createdAt;
    private Date expireAt;

    @ManyToOne
    @JoinColumn(name = "user_alias", referencedColumnName = "alias", foreignKey = @ForeignKey(name = "fk_RecoveryPassword_User_alias"))
    private User user;

    public RecoveryPassword(int code, Date createdAt, Date expireAt, User user) {
        this.code = code;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
        this.user = user;
    }
}
