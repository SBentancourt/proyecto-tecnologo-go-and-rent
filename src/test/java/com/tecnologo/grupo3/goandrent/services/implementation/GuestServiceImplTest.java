package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.AdminSignupBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.GuestSignUpBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.GuestWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.BookingRepository;
import com.tecnologo.grupo3.goandrent.repositories.GuestRepository;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    private Guest guest;
    private Booking booking;
    private String resultGuest = "prueba";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        guest = new Guest("guest", "guest@test.com", "guest", "nombre", "apellido",
                                UserStatus.ACEPTADO, new Date(), "00000", new Date(), "");
        booking = new Booking(new Date(), new Date(), BookingStatus.PENDIENTE, PaymentStatus.PENDIENTE, Float.parseFloat("10"), new Accommodation(), guest);

    }

    @Test
    void saveGuest() throws ParseException {
        when(guestRepository.save(guest)).thenReturn(guest);
        guestService.saveGuest(new GuestSignUpBodyDTO("guest", "guest@test.com", "guest", "nombre", "apellido", "00000", "01/01/2000", ""));
    }

    @Test
    void getGuestByEmail() {
        when(guestRepository.findGuestByEmail("guest@test.com")).thenReturn(Optional.ofNullable(guest));
        assertNotNull(guestService.getGuestByEmail("guest@test.com"));
    }

    @Test
    void getGuestByAlias() {
        when(guestRepository.findGuestByAlias("guest")).thenReturn(Optional.ofNullable(guest));
        assertNotNull(guestService.getGuestByAlias("guest"));
    }

    @Test
    void deleteGuest() {
        when(guestRepository.findGuestByAlias("guest")).thenReturn(Optional.of(guest));
        when(bookingRepository.guestWithBookingsInProgress("guest", new Date(), BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(false);
        guestService.deleteGuest("guest");
    }

    @Test
    void deleteGuest_withBookings() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = null;
        try {
            today = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        when(guestRepository.findGuestByAlias("guest")).thenReturn(Optional.of(guest));
        when(bookingRepository.guestWithBookingsInProgress("guest", today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(true);
        assertThrows(UserException.class, () -> guestService.deleteGuest("guest"));
    }

    @Test
    void getGuestsWithBookingsByHostAlias() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        when(guestRepository.getGuestWithBookingsByHostAlias("anfitrion1", BookingStatus.ACEPTADA, today, UserStatus.ELIMINADO)).thenReturn(Arrays.asList("guest"));
        when(bookingRepository.guestLastBooking("guest", "anfitrion1", BookingStatus.ACEPTADA, today)).thenReturn(Arrays.asList(booking));
        assertTrue(!guestService.getGuestsWithBookingsByHostAlias("anfitrion1").isEmpty());
    }
}