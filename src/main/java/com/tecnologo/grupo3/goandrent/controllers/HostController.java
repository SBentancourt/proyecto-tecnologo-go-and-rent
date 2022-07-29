package com.tecnologo.grupo3.goandrent.controllers;


import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.dtos.errors.ErrorDetail;
import com.tecnologo.grupo3.goandrent.dtos.responses.GenericResponse;
import com.tecnologo.grupo3.goandrent.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;


@RestController
@RequestMapping("/hosts")
@PreAuthorize("hasRole('HOST')")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class HostController {

    @Autowired
    private HostService hostService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private UserService userService;

    @PostMapping("/accommodation/add/{alias}")
    public ResponseEntity<?> addAccommodation(@PathVariable(value = "alias") String aliasHost,
                                              @RequestParam MultiValueMap<String, String> info, @RequestParam("images") MultipartFile[] imagenes) {
        if(hostService.existHostByAlias(aliasHost)){
            hostService.addAccommodation(info,imagenes,aliasHost,false);
            return new ResponseEntity<>(new GenericResponse("El nuevo alojamiento fue dado de alta con éxito."), HttpStatus.CREATED);
        }
        return new ResponseEntity<>( new ErrorDetail("El usuario que intenta dar de alta un alojamiento no es anfitrión."), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/accommodation/delete/{id}")
    public ResponseEntity<?> deleteAccommodation(@PathVariable(value = "id") int id) throws ParseException {
        accommodationService.deleteAccommodationById(id);
        return new ResponseEntity<>(new GenericResponse("Alojamiento eliminado correctamente."), HttpStatus.OK);
    }

    @GetMapping("/accommodation/list/{alias}")
    public ResponseEntity<?> accommodationList(@PathVariable(value = "alias") String alias){
        return new ResponseEntity<>(accommodationService.getAccommodationsByHost(alias), HttpStatus.OK);
    }

    @PostMapping("/accommodation/update/{id}")
    public ResponseEntity<?> updateAccommodation(@PathVariable(value = "id") int id, @RequestParam MultiValueMap<String, String> accInfo,
                                                 @RequestParam(value = "images", required = false) MultipartFile[] imagenes){
        accommodationService.updateAccommodationById(id, accInfo, imagenes);
        return new ResponseEntity<>(new GenericResponse("Alojamiento actualizado correctamente"), HttpStatus.OK);
    }

    @GetMapping("/bookings/{alias}")
    public ResponseEntity<?> hostBookings(@PathVariable(value = "alias") String alias, @RequestParam MultiValueMap<String, String> filtros,
                                          @RequestParam(value = "nroPag", defaultValue = "0", required = false) int nroPag,
                                          @RequestParam(value = "cantReg", defaultValue = "5", required = false) int cantReg) throws ParseException {

        return new ResponseEntity<>(bookingService.getHostBookings(alias, filtros, nroPag, cantReg), HttpStatus.OK);
    }

    @GetMapping("/list/guest/{alias}")
    public ResponseEntity<?> getListGuests(@PathVariable(value = "alias") String alias) throws ParseException {
        return new ResponseEntity<>(guestService.getGuestsWithBookingsByHostAlias(alias), HttpStatus.OK);
    }

    @PostMapping("/qualify-guest")
    public ResponseEntity<?> addQualifyToGuest(@RequestBody QualifyUserDTO qualifyUserDTO){
        userService.qualifyUser(qualifyUserDTO);
        return new ResponseEntity<>(new GenericResponse("Se calificó correctamente al usuario."), HttpStatus.OK);
    }

    @DeleteMapping("/qualify-guest/{host}/{guest}")
    public ResponseEntity<?> deleteGuestQualification(@PathVariable(value = "host") String hostAlias, @PathVariable(value = "guest") String guestAlias){
        userService.deleteGuestQualification(hostAlias, guestAlias);
        return new ResponseEntity<>(new GenericResponse("Se elimino la calificación correctamente."), HttpStatus.OK);
    }

    @GetMapping("/list/reviews/{id}")
    public ResponseEntity<?> getReviewsAccommodation(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(bookingService.getReviewsByAccommodationId(id), HttpStatus.OK);
    }


}
