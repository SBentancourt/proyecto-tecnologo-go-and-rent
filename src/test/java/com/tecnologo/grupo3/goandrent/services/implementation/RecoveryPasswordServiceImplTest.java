package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.RecoveryPassword;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.repositories.RecoveryPasswordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RecoveryPasswordServiceImplTest {

    @Mock
    private RecoveryPasswordRepository recoveryPasswordRepository;

    @InjectMocks
    private RecoveryPasswordServiceImpl recoveryPasswordService;

    private RecoveryPassword recoveryPassword;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recoveryPassword = new RecoveryPassword(1,123456, new Date(), new Date(), new User());
    }

    @Test
    void saveNewRecoveryPassword() {
        when(recoveryPasswordRepository.save(recoveryPassword)).thenReturn(recoveryPassword);
        recoveryPasswordService.saveNewRecoveryPassword(123456, new Date(), new Date(), new User());
    }

    @Test
    void isValidRecoveryCode() {
        when(recoveryPasswordRepository.findRecoveryPasswordByCodeAndUser(123456, new User())).thenReturn(Optional.ofNullable(recoveryPassword));
        assertNotNull(recoveryPasswordService.isValidRecoveryCode(123456, new User()));
    }
}