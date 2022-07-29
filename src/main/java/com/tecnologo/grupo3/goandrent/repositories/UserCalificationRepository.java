package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.UserCalification;
import com.tecnologo.grupo3.goandrent.entities.ids.UserCalificationID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCalificationRepository extends JpaRepository<UserCalification, UserCalificationID> {

    @Query(value = "select AVG(uc.qualification) from user_calification uc where uc.qualified_user = :alias", nativeQuery = true)
    Optional<Float> getAverageQualification(@Param("alias") String alias);

    Optional<UserCalification> findUserCalificationByUserCalificationID_QualifiedUserAndUserCalificationID_QualifyingUser(User qualifiedUser, String qualifyingUser);

    @Query(value = "select uc.qualification from user_calification uc where uc.qualifying_user =:qualifyingUser and uc.qualified_user =:qualifiedUser", nativeQuery = true)
    Optional<Integer> getUserCalificationByQualifyingAndQualifiedUsers(@Param("qualifyingUser") String qualifyingUser, @Param("qualifiedUser") String qualifiedUser);

}
