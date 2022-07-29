package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.*;
import com.tecnologo.grupo3.goandrent.dtos.errors.ErrorDetail;
import com.tecnologo.grupo3.goandrent.dtos.responses.GenericResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.ValidateCodeResponse;
import com.tecnologo.grupo3.goandrent.security.jwt.JwtTokenProvider;
import com.tecnologo.grupo3.goandrent.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class AuthController {

    @Autowired
    private GuestService guestService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostService hostService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup/guest")
    public ResponseEntity<?> signUpGuestUser(@RequestBody GuestSignUpBodyDTO guestDTO) throws ParseException {
        if (userService.existUserByAliasOrEmail(guestDTO.getAlias(), guestDTO.getEmail())){
            return new ResponseEntity<>(new ErrorDetail("El alias o el correo ingresado ya existe."), HttpStatus.BAD_REQUEST);
        }
        String passwordCod = passwordEncoder(guestDTO.getPassword());
        guestDTO.setPassword(passwordCod);
        guestService.saveGuest(guestDTO);
        return new ResponseEntity<>(new GenericResponse("Usuario registrado correctamente"), HttpStatus.CREATED);
    }

    @PostMapping("/signup/host")
    public ResponseEntity<?> signUpHostUser(@RequestParam MultiValueMap<String, String> info, @RequestParam("images") MultipartFile[] imagenes) throws ParseException {
        String alias = info.getFirst("alias");
        String email = info.getFirst("email");
        if (userService.existUserByAliasOrEmail(alias, email)){
            return new ResponseEntity<>("El alias o el correo ingresado ya existe.", HttpStatus.BAD_REQUEST);
        }
        // -- codifico la contraseña
        String pass = info.getFirst("password");
        String passwordCod = passwordEncoder(pass);
        info.set("password", passwordCod);
        hostService.saveHost(info, imagenes);


        return new ResponseEntity<>("Usuario registrado correctamente", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO infoLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(infoLogin.getEmail(), infoLogin.getPassword()));
        if (userService.isValidUser(infoLogin.getEmail())){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // -- se obtiene el token del JwtTokenProvider.
            String token = jwtTokenProvider.generarToken(authentication);
            // -- se devuelve el token obtenido.
            String roleUser = userService.getUserRoleByEmail(infoLogin.getEmail());
            String alias = userService.getUserAlias(infoLogin.getEmail());
            LoginResponse loginResponse = new LoginResponse(token, roleUser, userService.getUserName(infoLogin.getEmail()), alias);

            return ResponseEntity.ok(loginResponse);
        }
        ErrorDetail detalleDeError = new ErrorDetail("Hubo un problema para iniciar sesión con el usuario indicado. Contáctese con soporte.");
        return new ResponseEntity<>(detalleDeError, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login-device")
    public ResponseEntity<?> loginDevice(@RequestBody LoginDeviceDTO infoLogin){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(infoLogin.getEmail(), infoLogin.getPassword()));
        if (userService.isValidUser(infoLogin.getEmail())){
            String roleUser = userService.getUserRoleByEmail(infoLogin.getEmail());
            if (roleUser.equals("ROLE_GUEST")) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // -- se obtiene el token del JwtTokenProvider.
                String token = jwtTokenProvider.generarToken(authentication);
                // -- se devuelve el token obtenido.

                String alias = userService.getUserAlias(infoLogin.getEmail());
                deviceService.saveDevice(alias, infoLogin.getDeviceId());
                LoginResponse loginResponse = new LoginResponse(token, roleUser, userService.getUserName(infoLogin.getEmail()), alias);
                return ResponseEntity.ok(loginResponse);
            } else {
                ErrorDetail detalleDeError = new ErrorDetail("Los usuarios anfitriones o administradores NO puede iniciar sesión en la aplicación. Únicamente desde la web.");
                return new ResponseEntity<>(detalleDeError, HttpStatus.BAD_REQUEST);
            }
        }
        ErrorDetail detalleDeError = new ErrorDetail("Hubo un problema para iniciar sesión con el usuario indicado. Contáctese con soporte.");
        return new ResponseEntity<>(detalleDeError, HttpStatus.BAD_REQUEST);
    }





    @GetMapping("/recover-password/{email}")
    public ResponseEntity<?> recoverPassword(@PathVariable(value = "email") String email) throws ParseException {
        if (userService.isValidUser(email) && userService.existUserByAliasOrEmail(email, email)) {
            Integer code = getSixDigitsCode();
            // -- Obtengo vigencia del código
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            // -- Desde
            String fecha = format.format(calendar.getTime());
            Date startDate = format.parse(fecha);
            // -- Hasta
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 3);
            Date endDate = calendar.getTime();

            userService.recoverPassword(code, startDate, endDate, email);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        ErrorDetail detalleDeError = new ErrorDetail("Hubo un problema para recuperar la contraseña con el correo indicado. Contáctese con soporte.");
        return new ResponseEntity<>(detalleDeError, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/validate-code/{email}/{code}")
    public ResponseEntity<?> validateCode(@PathVariable(value = "email") String email, @PathVariable(value = "code") int code){
        return new ResponseEntity<>(new ValidateCodeResponse(userService.isValidCode(email, code)), HttpStatus.OK);
    }

    @PostMapping("/recover/change-password/{email}")
    public ResponseEntity<?> changePassword(@PathVariable(value = "email") String email, @RequestBody ChangePasswordDTO newPassword){
        if (userService.isValidCode(email, newPassword.getCodigo())) {
            String passwordCod = passwordEncoder(newPassword.getPassword());
            userService.changePassword(email, passwordCod);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        ErrorDetail detalleDeError = new ErrorDetail("El código de recuperación ha expirado.");
        return new ResponseEntity<>(detalleDeError, HttpStatus.BAD_REQUEST);
    }

    private String passwordEncoder (String clave){
        return  passwordEncoder.encode(clave);
    }

    private Integer getSixDigitsCode(){
        Double sixDigits = 100000 + Math.random() * 900000;
        return sixDigits.intValue();
    }
}
