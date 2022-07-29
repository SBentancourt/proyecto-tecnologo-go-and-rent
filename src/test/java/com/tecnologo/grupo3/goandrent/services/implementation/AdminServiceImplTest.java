package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.AdminSignupBodyDTO;
import com.tecnologo.grupo3.goandrent.entities.users_types.Admin;
import com.tecnologo.grupo3.goandrent.repositories.AdminRepository;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new Admin("admin", "admin@test.com", "admin", "nombre", "apellido", UserStatus.ACEPTADO, new Date(), "00000", new Date(), "");
    }

    @Test
    void getAdminByEmail() {
        when(adminRepository.findAdminByEmail("admin@test.com")).thenReturn(Optional.ofNullable(admin));
        assertNotNull(adminService.getAdminByEmail("admin@test.com"));
    }

    @Test
    void getAdminsEmails() {
        when(adminRepository.findAll()).thenReturn(Arrays.asList(admin));
        assertNotNull(adminService.getAdminsEmails());
    }

    @Test
    void saveAdmin() throws ParseException {
        when(adminRepository.save(admin)).thenReturn(admin);
        adminService.saveAdmin(new AdminSignupBodyDTO("admin", "admin@test.com", "admin", "nombre", "apellido", "00000", "01/01/2000", ""));
    }

    @Test
    void getStatistics() {
        List<Object[]> resulado1 = new ArrayList<>();
        Object[] res = new Object[6];
        res[0] = 1; res[1] = 2; res[2] = 3; res[3] = "prueba"; res[4] = "test"; res[5] = "est";
        resulado1.add(res);
        when(adminRepository.cantidadReservasPorRegionMes(0)).thenReturn(resulado1);
        List<Object[]> resulado2 = new ArrayList<>();
        Object[] res2 = new Object[4];
        res2[0] = 1; res2[1] = "GUEST"; res2[2] = 3; res2[3] = 4;
        resulado2.add(res2);
        when(adminRepository.cantidadUsuariosRegistradosPorMes()).thenReturn(resulado2);
        List<Object[]> resulado3 = new ArrayList<>();
        res[0] = 1; res[1] = 2;
        resulado3.add(res);
        when(adminRepository.cantidadAlojamientosPorCalificacion()).thenReturn(resulado3);
        List<Object[]> resulado4 = new ArrayList<>();
        res[0] = 1; res[1] = "prueba"; res[2] = "test"; res[3] = "est";
        resulado4.add(res);
        when(adminRepository.cantidadAlojamientosPorRegion()).thenReturn(resulado4);
        List<Object[]> resulado5 = new ArrayList<>();
        res[0] = 1; res[1] = 2; res[2] = 3;
        resulado5.add(res);
        when(adminRepository.cantidadAlijamientosRegistradosPorMes()).thenReturn(resulado5);

        assertNotNull(adminService.getStatistics());
    }
}