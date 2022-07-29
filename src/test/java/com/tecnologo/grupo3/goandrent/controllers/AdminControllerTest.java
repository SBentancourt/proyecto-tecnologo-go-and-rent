package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.AdminSignupBodyDTO;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private AdminService adminService;
    @Mock
    private HostService hostService;
    @Mock
    private BookingService bookingService;
    @Mock
    private AccommodationService accommodationService;
    @Mock
    private EMailService eMailService;

    @InjectMocks
    private AdminController adminController;

    private AdminSignupBodyDTO adminSignupBodyDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminSignupBodyDTO = new AdminSignupBodyDTO("alias", "email", "password", "name", "lastname",
                "12345", "10/10/2020", "");
    }

    @Test
    void signUpAdminUser() throws ParseException {
        when(userService.existUserByAliasOrEmail("alias", "email")).thenReturn(false);
        assertNotNull(adminController.signUpAdminUser(adminSignupBodyDTO));
    }

    @Test
    void signUpAdminUser_InvalidUser() throws ParseException {
        when(userService.existUserByAliasOrEmail("alias", "email")).thenReturn(true);
        assertNotNull(adminController.signUpAdminUser(adminSignupBodyDTO));
    }

    @Test
    void getUsers() {
        assertNotNull(adminController.getUsers());
    }

    @Test
    void blockUser_GUEST() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_GUEST");
        when(bookingService.guestWithBookingsInProgress("alias")).thenReturn(false);
        assertNotNull(adminController.blockUser("alias"));
    }

    @Test
    void blockUser_HOST() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_HOST");
        when(bookingService.hostWithBookingsInProgress("alias")).thenReturn(false);
        assertNotNull(adminController.blockUser("alias"));
    }

    @Test
    void blockUser_ADMIN() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_ADMIN");
        assertNotNull(adminController.blockUser("alias"));
    }

    @Test
    void blockUser_ActiveBookings() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_HOST");
        when(bookingService.hostWithBookingsInProgress("alias")).thenReturn(true);
        assertNotNull(adminController.blockUser("alias"));
    }

    @Test
    void unlockUser() {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_HOST");
        assertNotNull(adminController.unlockUser("alias"));
    }

    @Test
    void deleteUser_GUEST() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_GUEST");
        when(bookingService.guestWithBookingsInProgress("alias")).thenReturn(false);
        assertNotNull(adminController.deleteUser("alias"));
    }
    @Test
    void deleteUser_HOST() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_HOST");
        when(bookingService.hostWithBookingsInProgress("alias")).thenReturn(false);
        assertNotNull(adminController.deleteUser("alias"));
    }

    @Test
    void deleteUser_ActiveBookings() throws ParseException {
        when(userService.getUserRoleByAlias("alias")).thenReturn("ROLE_HOST");
        when(bookingService.hostWithBookingsInProgress("alias")).thenReturn(true);
        assertNotNull(adminController.deleteUser("alias"));
    }

    @Test
    void approveHost() {
        assertNotNull(adminController.approveHost("alias"));
    }

    @Test
    void rejectHost() {
        assertNotNull(adminController.rejectHost("alias"));
    }

    @Test
    void getStatistics() {
        assertNotNull(adminController.getStatistics());
    }
}