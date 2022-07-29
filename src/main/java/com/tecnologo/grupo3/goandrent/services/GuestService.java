package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.GuestSignUpBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.GuestWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface GuestService {
    void saveGuest(GuestSignUpBodyDTO guest) throws ParseException;
    Optional<Guest> getGuestByEmail(String email);
    Optional<Guest> getGuestByAlias(String alias);
    void deleteGuest(String alias);

    List<GuestWithBookingsResponse> getGuestsWithBookingsByHostAlias(String alias) throws ParseException;
}
