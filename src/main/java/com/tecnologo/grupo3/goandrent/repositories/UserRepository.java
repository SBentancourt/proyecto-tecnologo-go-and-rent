package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsUserByAliasOrEmail(String alias, String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByAlias(String alias);
    Boolean existsUserByEmailAndUserStatus(String email, UserStatus status);
}
