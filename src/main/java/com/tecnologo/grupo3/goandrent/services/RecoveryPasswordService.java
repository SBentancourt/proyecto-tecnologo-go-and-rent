package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.entities.User;

import java.util.Date;

public interface RecoveryPasswordService {
    void saveNewRecoveryPassword(int code, Date startDate, Date endDate, User user);
    Boolean isValidRecoveryCode(int code, User user);
}
