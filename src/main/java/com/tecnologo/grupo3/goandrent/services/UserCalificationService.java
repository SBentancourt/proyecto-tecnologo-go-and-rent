package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.entities.User;

public interface UserCalificationService {
    Float getUserQualification(String alias);
    void addUserQualification(String qualifyingUser, String qualifiedUser, int qualification, User qualifyingUserObj, User qualifiedUserObj);
    void deleteQualification(String qualifyingUser, User qualifiedUser);
    Integer getUserQualificationByQualifyingAndQualifiedUsers(String qualifyingUser, String qualifiedUser);
}
