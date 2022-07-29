package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.AdminSignupBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.HostBookingsPaymentsDTO;
import com.tecnologo.grupo3.goandrent.dtos.errors.ErrorDetail;
import com.tecnologo.grupo3.goandrent.dtos.responses.GenericResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.UsersListResponse;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.utils.PDFExporter;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private HostService hostService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private EMailService eMailService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpAdminUser(@RequestBody AdminSignupBodyDTO adminDTO) throws ParseException {
        if (userService.existUserByAliasOrEmail(adminDTO.getAlias(), adminDTO.getEmail())){
            return new ResponseEntity<>(new ErrorDetail("El alias o el correo ingresado ya existe."), HttpStatus.BAD_REQUEST);
        }
        String passwordCod = passwordEncoder(adminDTO.getPassword());
        adminDTO.setPassword(passwordCod);
        adminService.saveAdmin(adminDTO);
        return new ResponseEntity<>(new GenericResponse("Usuario registrado correctamente"), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return new ResponseEntity<>(new UsersListResponse(userService.getUsers()), HttpStatus.OK);
    }

    @PutMapping("/block/{alias}")
    public ResponseEntity<?> blockUser(@PathVariable(value = "alias") String alias) throws ParseException {
        String roleUser = userService.getUserRoleByAlias(alias);
        Boolean cantBeLocked = false;
        if (roleUser.equals("ROLE_GUEST"))
            cantBeLocked = bookingService.guestWithBookingsInProgress(alias);
        else if (roleUser.equals("ROLE_HOST"))
            cantBeLocked = bookingService.hostWithBookingsInProgress(alias);
        else
            return new ResponseEntity<>(new ErrorDetail("Los usuarios administradores no se pueden bloquear"), HttpStatus.BAD_REQUEST);

        if (!cantBeLocked){
            userService.updateUserStatus(alias, UserStatus.BLOQUEADO);
            // -- Si el usuario es anfitrión tambien hay que actualizar el estado de sus alojamientos
            if (roleUser.equals("ROLE_HOST"))
                hostService.updateAccommodationStatus(alias, AccommodationStatus.BLOQUEADO);

            userService.notifyStatusAccount(alias, UserStatus.BLOQUEADO);
            return new ResponseEntity<>(new GenericResponse("Se bloqueó el usuario correctamente"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorDetail("El usuario tiene reservas activas y no se puede bloquear"), HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/unlock/{alias}")
    public ResponseEntity<?> unlockUser(@PathVariable(value = "alias") String alias){
        String roleUser = userService.getUserRoleByAlias(alias);
        if (roleUser.equals("ROLE_HOST"))
            hostService.updateAccommodationStatus(alias, AccommodationStatus.ACTIVO);

        userService.updateUserStatus(alias, UserStatus.ACEPTADO);
        userService.notifyStatusAccount(alias, UserStatus.ACEPTADO);
        return new ResponseEntity<>(new GenericResponse("Se desbloqueó el usuario correctamente"), HttpStatus.OK);
    }

    @PutMapping("/delete-user/{alias}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "alias") String alias) throws ParseException {
        String roleUser = userService.getUserRoleByAlias(alias);
        // -- Si el usuario es huesped o anfitrion hay que verificar que no tenga reservas en curso
        Boolean cantBeDeleted = false;
        if (roleUser.equals("ROLE_GUEST"))
            cantBeDeleted = bookingService.guestWithBookingsInProgress(alias);
        else if (roleUser.equals("ROLE_HOST"))
            cantBeDeleted = bookingService.hostWithBookingsInProgress(alias);
        if (!cantBeDeleted){
            userService.updateUserStatus(alias, UserStatus.ELIMINADO);
            // -- Si el usuario es anfitrión también hay que actualizar el estado de sus alojamientos
            if (roleUser.equals("ROLE_HOST"))
                hostService.updateAccommodationStatus(alias, AccommodationStatus.BLOQUEADO);

            return new ResponseEntity<>(new GenericResponse("Se eliminó el usuario correctamente"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorDetail("El usuario tiene reservas activas y no se puede eliminar"), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/approve-host/{alias}")
    public ResponseEntity<?> approveHost(@PathVariable(value = "alias") String alias){
        hostService.updateAccommodationStatus(alias, AccommodationStatus.ACTIVO);
        userService.updateUserStatus(alias, UserStatus.ACEPTADO);
        return new ResponseEntity<>(new GenericResponse("Se aprobó el usuario correctamente."), HttpStatus.OK);
    }

    @PutMapping("/reject-host/{alias}")
    public ResponseEntity<?> rejectHost(@PathVariable(value = "alias") String alias){
        userService.updateUserStatus(alias, UserStatus.ELIMINADO);
        accommodationService.deleteAccommodationPhotosByHostAlias(alias);
        eMailService.sendEmailRejectHost(alias);
        return new ResponseEntity<>(new GenericResponse("Se rechazó correctamente al usuario"), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(){
        return new ResponseEntity<>(adminService.getStatistics(), HttpStatus.OK);
    }

    @GetMapping("/payments/{month}/{year}")
    public void getHostPayments(@PathVariable(value = "month") int month, @PathVariable(value = "year") int year, HttpServletResponse response) throws ParseException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=hosts_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);

        List<HostBookingsPaymentsDTO> listHost = adminService.getHostsPaymentsMonthYear(month, year);
        PDFExporter exporter = new PDFExporter(listHost);
        exporter.export(response);
    }


    private String passwordEncoder (String clave){
        return  passwordEncoder.encode(clave);
    }
}
