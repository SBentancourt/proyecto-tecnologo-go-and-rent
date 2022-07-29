package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.GuestSignUpBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.GuestWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.exceptions.AccommodationException;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.BookingRepository;
import com.tecnologo.grupo3.goandrent.repositories.GuestRepository;
import com.tecnologo.grupo3.goandrent.services.GuestService;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void saveGuest(GuestSignUpBodyDTO guest) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date birthday = format.parse(guest.getBirthday());

        Guest newGuest = guestRepository.save(new Guest(guest.getAlias(), guest.getEmail(), guest.getPassword(),
                                            guest.getName(), guest.getLastName(), UserStatus.ACEPTADO, new Date(),
                                            guest.getPhone(), birthday, guest.getPicture()));

    }

    @Override
    public Optional<Guest> getGuestByEmail(String email) {
        return guestRepository.findGuestByEmail(email);
    }

    @Override
    public Optional<Guest> getGuestByAlias(String alias) {
        return guestRepository.findGuestByAlias(alias);
    }

    @Override
    public void deleteGuest(String alias) {
        Guest guest = getGuestByAlias(alias)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe usuario con alias: " + alias));

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = null;
        try {
            today = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Boolean existBookings = bookingRepository.guestWithBookingsInProgress(alias, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE);
        if (bookingRepository.guestWithBookingsInProgress(alias, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)){
            throw new UserException(HttpStatus.BAD_REQUEST, "No puede eliminar su usuario mientras tenga reservas activas o pendientes.");
        }

        guest.setUserStatus(UserStatus.ELIMINADO);
        guestRepository.save(guest);
    }

    @Override
    public List<GuestWithBookingsResponse> getGuestsWithBookingsByHostAlias(String alias) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        List<String> guests = guestRepository.getGuestWithBookingsByHostAlias(alias, BookingStatus.ACEPTADA, today, UserStatus.ELIMINADO);
        List<GuestWithBookingsResponse> responses = new ArrayList<>();
        for (String g: guests){
            List<Booking> bookings = bookingRepository.guestLastBooking(g, alias, BookingStatus.ACEPTADA, today);
            Booking booking = bookings.get(0);
            responses.add(new GuestWithBookingsResponse(g, booking.getGuest().getName(), booking.getGuest().getLastName(),
                    booking.getId(), booking.getAccommodation().getName(), format.format(booking.getStartDate()),
                    format.format(booking.getEndDate())));
        }
        return responses;
    }
}
