package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.RecoveryPassword;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.exceptions.RecoveryPasswordException;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.RecoveryPasswordRepository;
import com.tecnologo.grupo3.goandrent.services.RecoveryPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RecoveryPasswordServiceImpl implements RecoveryPasswordService {

    @Autowired
    private RecoveryPasswordRepository recoveryPasswordRepository;

    @Override
    public void saveNewRecoveryPassword(int code, Date startDate, Date endDate, User user) {
        recoveryPasswordRepository.save(new RecoveryPassword(code, startDate, endDate, user));
    }

    @Override
    public Boolean isValidRecoveryCode(int code, User user) {
        RecoveryPassword recoveryPassword = recoveryPasswordRepository.findRecoveryPasswordByCodeAndUser(code, user)
                .orElseThrow(() -> new RecoveryPasswordException(HttpStatus.NOT_FOUND, "El código ingresado no es correcto."));

        return recoveryPassword.getExpireAt().after(new Date());
    }
}
