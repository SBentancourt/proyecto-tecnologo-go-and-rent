package com.tecnologo.grupo3.goandrent.entities;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iUser02", columnList = "email", unique = true),
                    @Index(name = "iUser03", columnList = "role")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @Column(name = "alias", unique = true, nullable = false)
    private String alias;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private UserStatus userStatus;
    private Date createdAt;
    private String phone;
    private Date birthday;
    private String picture;
}
