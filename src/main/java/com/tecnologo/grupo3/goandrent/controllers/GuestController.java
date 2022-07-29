package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.*;
import com.tecnologo.grupo3.goandrent.dtos.responses.GenericResponse;
import com.tecnologo.grupo3.goandrent.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;

@RestController
@RequestMapping("/guests")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
@PreAuthorize("hasRole('GUEST')")
public class GuestController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private HostService hostService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/booking/verify")
    public ResponseEntity<?> verifyAccommodationBooking(@RequestParam(value = "id", required = true) int id,
                                                        @RequestParam(value = "start_date", required = true) String startDate,
                                                        @RequestParam(value = "end_date", required = true) String endDate) throws ParseException {
        String[] split = startDate.split("-",3);
        String day = split[0].trim();
        String mm = split[1].trim();
        String year = split[2].trim();
        String start_date = day + "/" + mm + "/" + year;
        String[] split2 = endDate.split("-",3);
        day = split2[0].trim();
        mm = split2[1].trim();
        year = split2[2].trim();
        String end_date = day + "/" + mm + "/" + year;

        VerifyBookingDTO verifyBookingDTO = new VerifyBookingDTO();
        verifyBookingDTO.setId_accommodation(id);
        verifyBookingDTO.setStart_date(start_date);
        verifyBookingDTO.setEnd_date(end_date);
        return new ResponseEntity<>(bookingService.existBookingsDateRange(verifyBookingDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{alias}")
    public ResponseEntity<?> deleteAccount(@PathVariable(value = "alias") String alias){
        guestService.deleteGuest(alias);
        return new ResponseEntity<>(new GenericResponse("Usuario eliminado correctamente"),HttpStatus.OK);
    }

    @GetMapping("/bookings/{alias}")
    public ResponseEntity<?> getGuestBookings(@PathVariable(value = "alias") String alias) {
        return new ResponseEntity<>(bookingService.getGuestBookings(alias), HttpStatus.OK);
    }

    @GetMapping("/bookings-info/{alias}")
    public ResponseEntity<?> getGuestBookingsAllInfo(@PathVariable(value = "alias") String alias) {
        return new ResponseEntity<>(bookingService.getGuestBookingsWithAllInfo(alias), HttpStatus.OK);
    }

    @GetMapping("/booking/detail/{id}")
    public ResponseEntity<?> getDetailGuestBooking(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(bookingService.getBookingDetailByBookingId(id), HttpStatus.OK);
    }

    @GetMapping("/list/hosts/{alias}")
    public ResponseEntity<?> getHostsList(@PathVariable(value = "alias") String alias) throws ParseException {
        return new ResponseEntity<>(hostService.getHostWithBookingByGuestAlias(alias), HttpStatus.OK);
    }

    @PostMapping("/review/add")
    public ResponseEntity<?> addReview(@RequestBody AddReviewDTO reviewDTO){
        bookingService.addBookingReview(reviewDTO);
        return new ResponseEntity<>(new GenericResponse("La reseña fue agregada correctamente."),HttpStatus.CREATED);
    }

    @PutMapping("/review/update")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewDTO reviewDTO){
        reviewService.updateReview(reviewDTO);
        return new ResponseEntity<>(new GenericResponse("La reseña fue modificada correctamente."),HttpStatus.OK);
    }

    @PostMapping("/qualify-host")
    public ResponseEntity<?> addQualifyToHost(@RequestBody QualifyUserDTO qualifyUserDTO){
        userService.qualifyUserHost(qualifyUserDTO);
        return new ResponseEntity<>(new GenericResponse("Se calificó correctamente al usuario."), HttpStatus.OK);
    }

    @DeleteMapping("/qualify-host/{guest}/{host}")
    public ResponseEntity<?> deleteHostQualification(@PathVariable(value = "host") String hostAlias, @PathVariable(value = "guest") String guestAlias){
        userService.deleteHostQualification(hostAlias, guestAlias);
        return new ResponseEntity<>(new GenericResponse("Se elimino la calificación correctamente."), HttpStatus.OK);
    }

    @PostMapping("/favorites")
    public ResponseEntity<?> addOrDeleteFavorites(@RequestBody FavoriteDTO favoriteDTO){
        favoriteService.addOrDeleteFavorites(favoriteDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/favorites/{alias}")
    public ResponseEntity<?> getFavorites(@PathVariable(value = "alias") String alias){
        return new ResponseEntity<>(favoriteService.getFavorites(alias), HttpStatus.OK);
    }

}
