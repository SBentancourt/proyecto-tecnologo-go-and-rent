package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.InfoProfileDTO;
import com.tecnologo.grupo3.goandrent.dtos.UpdatePasswordDTO;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UsersControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UsersController usersController;

    private InfoProfileDTO infoProfileDTO;
    private UpdatePasswordDTO updatePasswordDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        infoProfileDTO = new InfoProfileDTO("alias", "email", "name", "lastname", "12345",
            "10/01/1990", "");
        updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setOldPassword("vieja");
        updatePasswordDTO.setNewPassword("nueva");
        updatePasswordDTO.setAlias("alias");
    }

    @Test
    void userProfile() {
        when(userService.getInfoProfile("alias")).thenReturn(infoProfileDTO);
        assertNotNull(usersController.userProfile("alias"));
    }

    @Test
    void testUserProfile() throws ParseException {
        assertNotNull(usersController.updateUserProfile(infoProfileDTO));
    }

    @Test
    void updatePasswordProfile() {
        assertNotNull(usersController.updatePasswordProfile(updatePasswordDTO));
    }
}