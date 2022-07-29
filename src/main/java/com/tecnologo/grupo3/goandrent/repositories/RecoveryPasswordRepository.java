package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.RecoveryPassword;
import com.tecnologo.grupo3.goandrent.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface RecoveryPasswordRepository extends JpaRepository<RecoveryPassword, Integer> {
    Boolean existsRecoveryPasswordByCodeAndUserAndExpireAtIsGreaterThan(int code, User user, Date date);
    Optional<RecoveryPassword> findRecoveryPasswordByCodeAndUser(int code, User user);
}
