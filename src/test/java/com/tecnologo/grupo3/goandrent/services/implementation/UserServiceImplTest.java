package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.InfoProfileDTO;
import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.dtos.UpdatePasswordDTO;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.UserRepository;
import com.tecnologo.grupo3.goandrent.services.AdminService;
import com.tecnologo.grupo3.goandrent.services.HostService;
import com.tecnologo.grupo3.goandrent.services.RecoveryPasswordService;
import com.tecnologo.grupo3.goandrent.services.UserCalificationService;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.utils.enums.Bank;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HostService hostService;
    @Mock
    private AdminService adminService;
    @Mock
    private RecoveryPasswordService recoveryPasswordService;
    @Mock
    private UserCalificationService userCalificationService;
    @Mock
    private EMailService eMailService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private User user_eliminated;
    private Guest guest;
    private Host host;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("alias", "email", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "012345", new Date(), "");
        user_eliminated = new User("alias", "email", "password", "name", "lastname", UserStatus.ELIMINADO, new Date(), "012345", new Date(), "");
        guest = new Guest("guest", "guest", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "012345", new Date(), "");
        host = new Host("host", "host", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "012345", new Date(), "", Bank.ITAU, "01234");
    }

    @Test
    void existUserByAliasOrEmail() {
        when(userRepository.existsUserByAliasOrEmail("alias", "email")).thenReturn(true);
        assertTrue(userService.existUserByAliasOrEmail("alias", "email"));
    }

    @Test
    void recoverPassword() {
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        userService.recoverPassword(1, new Date(), new Date(), "email");
    }

    @Test
    void recoverPassword_WrongStatus() {
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user_eliminated));
        assertThrows(UserException.class, () -> userService.recoverPassword(1, new Date(), new Date(), "email"));
    }

    @Test
    void isValidCode() {
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        when(recoveryPasswordService.isValidRecoveryCode(123456, user)).thenReturn(true);
        assertTrue(userService.isValidCode("email", 123456));
    }

    @Test
    void changePassword() {
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        userService.changePassword("email", "password");
    }

    @Test
    void changePassword_WrongStatus() {
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user_eliminated));
        assertThrows(UserException.class, () -> userService.changePassword("email", "password"));
    }

    @Test
    void getUsers() {
        //Guest userGuest = new Guest("guest", "guest", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "012345", new Date(), "");
        when(userRepository.findAll()).thenReturn(Arrays.asList(guest));
        when(userCalificationService.getUserQualification("guest")).thenReturn(Float.parseFloat("10"));
        assertNotNull(userService.getUsers());
    }

    @Test
    void getUserRoleByEmail() {
        when(userRepository.findUserByEmail("guest")).thenReturn(Optional.of(guest));
        assertTrue(userService.getUserRoleByEmail("guest").equals("ROLE_GUEST"));
    }

    @Test
    void getUserRoleByAlias() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        assertTrue(userService.getUserRoleByAlias("guest").equals("ROLE_GUEST"));
    }

    @Test
    void updateUserStatus() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        userService.updateUserStatus("guest", UserStatus.BLOQUEADO);
    }

    @Test
    void notifyStatusAccount() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        userService.notifyStatusAccount("guest", UserStatus.BLOQUEADO);
    }

    @Test
    void getInfoProfile_GUEST() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        when(userCalificationService.getUserQualification("guest")).thenReturn(Float.parseFloat("10"));
        assertNotNull(userService.getInfoProfile("guest"));
    }

    @Test
    void getInfoProfile_HOST() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(hostService.getHostByEmail("host")).thenReturn(Optional.of(host));
        when(userCalificationService.getUserQualification("host")).thenReturn(Float.parseFloat("10"));
        assertNotNull(userService.getInfoProfile("host"));
    }

    @Test
    void updateUserInformation() throws ParseException {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(hostService.getHostByEmail("host")).thenReturn(Optional.of(host));
        InfoProfileDTO infoProfileDTO = new InfoProfileDTO("host", "host", "name", "lastname", "012345", "01/01/2000", "");
        infoProfileDTO.setBank(Bank.SCOTIABANK.toString());
        infoProfileDTO.setAccount("010010101");
        userService.updateUserInformation(infoProfileDTO);
    }

    @Test
    void isValidUser() {
        when(userRepository.existsUserByEmailAndUserStatus("host", UserStatus.ACEPTADO)).thenReturn(true);
        userService.isValidUser("host");
    }

    @Test
    void getUserName() {
        when(userRepository.findUserByEmail("host")).thenReturn(Optional.of(host));
        assertTrue(userService.getUserName("host").equals("name"));
    }

    @Test
    void getUserAlias() {
        when(userRepository.findUserByEmail("host")).thenReturn(Optional.of(host));
        assertTrue(userService.getUserAlias("host").equals("host"));
    }

    @Test
    void updatePasswordProfile() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("nueva");
        updatePasswordDTO.setOldPassword("password");
        updatePasswordDTO.setAlias("host");
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        userService.updatePasswordProfile(updatePasswordDTO);
    }
    @Test
    void updatePasswordProfile_Error() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("nueva");
        updatePasswordDTO.setOldPassword("password");
        updatePasswordDTO.setAlias("host");
        when(passwordEncoder.matches("password", "password")).thenReturn(false);
        assertThrows(UserException.class, () -> userService.updatePasswordProfile(updatePasswordDTO));
    }

    @Test
    void getUserByAlias() {
        when(userRepository.findUserByAlias("alias")).thenReturn(Optional.of(user));
        assertNotNull(userService.getUserByAlias("alias"));
    }

    @Test
    void qualifyUser() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        QualifyUserDTO qualifyUserDTO = new QualifyUserDTO();
        qualifyUserDTO.setQualifyingUser("host");
        qualifyUserDTO.setQualifiedUser("guest");
        qualifyUserDTO.setQualification(3);
        userService.qualifyUser(qualifyUserDTO);
    }
    @Test
    void qualifyUser_HOST_Error() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(guest));
        QualifyUserDTO qualifyUserDTO = new QualifyUserDTO();
        qualifyUserDTO.setQualifyingUser("host");
        qualifyUserDTO.setQualifiedUser("guest");
        qualifyUserDTO.setQualification(3);
        assertThrows(UserException.class, () -> userService.qualifyUser(qualifyUserDTO));
    }

    @Test
    void qualifyUser_GUEST_Error() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(host));
        QualifyUserDTO qualifyUserDTO = new QualifyUserDTO();
        qualifyUserDTO.setQualifyingUser("host");
        qualifyUserDTO.setQualifiedUser("guest");
        qualifyUserDTO.setQualification(3);
        assertThrows(UserException.class, () -> userService.qualifyUser(qualifyUserDTO));
    }

    @Test
    void deleteGuestQualification() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        userService.deleteGuestQualification("host", "guest");
    }

    @Test
    void deleteGuestQualification_HOST_Error() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(guest));
        assertThrows(UserException.class, () -> userService.deleteGuestQualification("host", "guest"));
    }

    @Test
    void deleteGuestQualification_GUEST_Error() {
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(host));
        assertThrows(UserException.class, () -> userService.deleteGuestQualification("host", "guest"));
    }

    @Test
    void qualifyUserHost() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        QualifyUserDTO qualifyUserDTO = new QualifyUserDTO();
        qualifyUserDTO.setQualifyingUser("guest");
        qualifyUserDTO.setQualifiedUser("host");
        qualifyUserDTO.setQualification(3);
        userService.qualifyUserHost(qualifyUserDTO);
    }

    @Test
    void qualifyUserHost_GUEST_Error() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(host));
        QualifyUserDTO qualifyUserDTO = new QualifyUserDTO();
        qualifyUserDTO.setQualifyingUser("guest");
        qualifyUserDTO.setQualifiedUser("host");
        qualifyUserDTO.setQualification(3);
        assertThrows(UserException.class, () -> userService.qualifyUserHost(qualifyUserDTO));
    }

    @Test
    void qualifyUserHost_HOST_Error() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(guest));
        QualifyUserDTO qualifyUserDTO = new QualifyUserDTO();
        qualifyUserDTO.setQualifyingUser("guest");
        qualifyUserDTO.setQualifiedUser("host");
        qualifyUserDTO.setQualification(3);
        assertThrows(UserException.class, () -> userService.qualifyUserHost(qualifyUserDTO));
    }

    @Test
    void deleteHostQualification() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(host));
        userService.deleteHostQualification("host", "guest");
    }

    @Test
    void deleteHostQualification_GUEST_Error() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(host));
        assertThrows(UserException.class, () -> userService.deleteHostQualification("host", "guest"));
    }

    @Test
    void deleteHostQualification_HOST_Error() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        when(userRepository.findUserByAlias("host")).thenReturn(Optional.of(guest));
        assertThrows(UserException.class, () -> userService.deleteHostQualification("host", "guest"));
    }
}