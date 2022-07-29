package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.UserCalification;
import com.tecnologo.grupo3.goandrent.entities.ids.UserCalificationID;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.UserCalificationRepository;
import com.tecnologo.grupo3.goandrent.repositories.UserRepository;
import com.tecnologo.grupo3.goandrent.services.UserCalificationService;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.DiscriminatorValue;
import java.util.Optional;

@Service
public class UserCalificationServiceImpl implements UserCalificationService {

    @Autowired
    private UserCalificationRepository userCalificationRepository;

    @Override
    public Float getUserQualification(String alias) {
        Optional<Float> qualification = userCalificationRepository.getAverageQualification(alias);
        return qualification.orElseGet(() -> Float.valueOf(0));
    }

    @Override
    public void addUserQualification(String qualifyingUser, String qualifiedUser, int qualification, User qualifyingUserObj, User qualifiedUserObj) {
        UserCalificationID id = new UserCalificationID(qualifyingUser, qualifiedUserObj);
        userCalificationRepository.save(new UserCalification(id, qualification, qualifyingUserObj));
    }

    @Override
    public void deleteQualification(String qualifyingUser, User qualifiedUser) {
        UserCalification userCalification = userCalificationRepository
                .findUserCalificationByUserCalificationID_QualifiedUserAndUserCalificationID_QualifyingUser(qualifiedUser, qualifyingUser)
                .orElseThrow(
                        () -> new UserException(HttpStatus.NOT_FOUND, "No existe una calificación entre los usuarios " + qualifyingUser + " y " + qualifiedUser.getAlias()));
        userCalificationRepository.delete(userCalification);
    }

    @Override
    public Integer getUserQualificationByQualifyingAndQualifiedUsers(String qualifyingUser, String qualifiedUser) {
        return userCalificationRepository.getUserCalificationByQualifyingAndQualifiedUsers(qualifyingUser, qualifiedUser).orElse(0);
    }
}
