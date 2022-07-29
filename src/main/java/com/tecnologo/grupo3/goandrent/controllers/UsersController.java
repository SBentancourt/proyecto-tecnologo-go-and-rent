package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.InfoProfileDTO;
import com.tecnologo.grupo3.goandrent.dtos.UpdatePasswordDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.GenericResponse;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile/{alias}")
    public ResponseEntity<?> userProfile(@PathVariable(value = "alias") String alias){
        return new ResponseEntity<>(userService.getInfoProfile(alias), HttpStatus.OK);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody InfoProfileDTO infoUser) throws ParseException {
        userService.updateUserInformation(infoUser);
        return new ResponseEntity<>(new GenericResponse("Se actualizó la información del usuario correctamente"), HttpStatus.CREATED);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePasswordProfile(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        userService.updatePasswordProfile(updatePasswordDTO);
        return new ResponseEntity<>(new GenericResponse("Se actualizó la contraseña correctamente"), HttpStatus.OK);
    }
}
