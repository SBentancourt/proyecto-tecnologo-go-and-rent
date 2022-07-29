package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.ChangePasswordDTO;
import com.tecnologo.grupo3.goandrent.dtos.GuestSignUpBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.LoginDTO;
import com.tecnologo.grupo3.goandrent.dtos.LoginDeviceDTO;
import com.tecnologo.grupo3.goandrent.security.jwt.JwtTokenProvider;
import com.tecnologo.grupo3.goandrent.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private GuestService guestService;
    @Mock
    private UserService userService;
    @Mock
    private HostService hostService;
    @Mock
    private AdminService adminService;
    @Mock
    private DeviceService deviceService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private GuestSignUpBodyDTO guestSignUpBodyDTO;
    private MultiValueMap<String, String> info = new LinkedMultiValueMap<String, String>();
    private MultipartFile[] imagenes = new MultipartFile[1];
    private LoginDTO loginDTO = new LoginDTO();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        guestSignUpBodyDTO = new GuestSignUpBodyDTO("alias", "email", "password", "name", "lastname",
                                                        "12345", "10/10/2010", "");

        info.add("alias", "alias");
        info.add("email", "email");
        info.add("password", "password");

        loginDTO.setPassword("password");
        loginDTO.setEmail("email");
    }

    @Test
    void signUpGuestUser() throws ParseException {
        when(userService.existUserByAliasOrEmail("alias", "email")).thenReturn(false);
        assertNotNull(authController.signUpGuestUser(guestSignUpBodyDTO));
    }

    @Test
    void signUpGuestUser_InvalidUser() throws ParseException {
        when(userService.existUserByAliasOrEmail("alias", "email")).thenReturn(true);
        assertNotNull(authController.signUpGuestUser(guestSignUpBodyDTO));
    }

    @Test
    void signUpHostUser() throws ParseException {
        when(userService.existUserByAliasOrEmail("alias", "email")).thenReturn(false);
        assertNotNull(authController.signUpHostUser(info, imagenes));
    }
    @Test
    void signUpHostUser_InvalidUser() throws ParseException {
        when(userService.existUserByAliasOrEmail("alias", "email")).thenReturn(true);
        assertNotNull(authController.signUpHostUser(info, imagenes));
    }

    @Test
    void login() {
        when(userService.isValidUser("email")).thenReturn(true);
        when(userService.getUserRoleByEmail("email")).thenReturn("ROLE_GUEST");
        when(userService.getUserAlias("email")).thenReturn("alias");
        assertNotNull(authController.login(loginDTO));
    }

    @Test
    void login_InvalidUser() {
        when(userService.isValidUser("email")).thenReturn(false);
        assertNotNull(authController.login(loginDTO));
    }

    @Test
    void recoverPassword() throws ParseException {
        when(userService.isValidUser("email")).thenReturn(true);
        when(userService.existUserByAliasOrEmail("email", "email")).thenReturn(true);
        assertNotNull(authController.recoverPassword("email"));
    }
    @Test
    void recoverPassword_InvalidUser() throws ParseException {
        when(userService.isValidUser("email")).thenReturn(false);
        when(userService.existUserByAliasOrEmail("email", "email")).thenReturn(true);
        assertNotNull(authController.recoverPassword("email"));
    }

    @Test
    void validateCode() {
        when(userService.isValidCode("email", 123456)).thenReturn(true);
        assertNotNull(authController.validateCode("email", 123456));
    }

    @Test
    void changePassword() {
        when(userService.isValidCode("email", 123456)).thenReturn(true);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setPassword("nueva");
        changePasswordDTO.setCodigo(123456);
        assertNotNull(authController.changePassword("email", changePasswordDTO));
    }
    @Test
    void changePassword_InvalidCode() {
        when(userService.isValidCode("email", 123456)).thenReturn(false);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setPassword("nueva");
        changePasswordDTO.setCodigo(123456);
        assertNotNull(authController.changePassword("email", changePasswordDTO));
    }

    @Test
    void loginDevice() {
        when(userService.isValidUser("email")).thenReturn(true);
        when(userService.getUserRoleByEmail("email")).thenReturn("ROLE_GUEST");
        when(userService.getUserAlias("email")).thenReturn("alias");
        LoginDeviceDTO loginDeviceDTO = new LoginDeviceDTO();
        loginDeviceDTO.setDeviceId("1");
        loginDeviceDTO.setPassword("1234");
        loginDeviceDTO.setEmail("email");
        assertNotNull(authController.loginDevice(loginDeviceDTO));
    }
    @Test
    void loginDevice_UserHOST() {
        when(userService.isValidUser("email")).thenReturn(true);
        when(userService.getUserRoleByEmail("email")).thenReturn("ROLE_HOST");
        LoginDeviceDTO loginDeviceDTO = new LoginDeviceDTO();
        loginDeviceDTO.setDeviceId("1");
        loginDeviceDTO.setPassword("1234");
        loginDeviceDTO.setEmail("email");
        assertNotNull(authController.loginDevice(loginDeviceDTO));
    }
    @Test
    void loginDevice_InvalidUser() {
        when(userService.isValidUser("email")).thenReturn(false);
        LoginDeviceDTO loginDeviceDTO = new LoginDeviceDTO();
        loginDeviceDTO.setDeviceId("1");
        loginDeviceDTO.setPassword("1234");
        loginDeviceDTO.setEmail("email");
        assertNotNull(authController.loginDevice(loginDeviceDTO));
    }
}