package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.*;
import com.tecnologo.grupo3.goandrent.dtos.responses.GuestBookingInfoDetailResponse;
import com.tecnologo.grupo3.goandrent.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GuestControllerTest {

    @Mock
    private BookingService bookingService;
    @Mock
    private GuestService guestService;
    @Mock
    private HostService hostService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private UserService userService;
    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private GuestController guestController;

    private VerifyBookingDTO verifyBookingDTO = new VerifyBookingDTO();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyAccommodationBooking() throws ParseException {
        when(bookingService.existBookingsDateRange(verifyBookingDTO)).thenReturn(true);
        assertNotNull(guestController.verifyAccommodationBooking(1, "10-10-2020", "11-10-2020"));
    }

    @Test
    void deleteAccount() {
        assertNotNull(guestController.deleteAccount("alias"));
    }

    @Test
    void getGuestBookings() {
        assertNotNull(guestController.getGuestBookings("alias"));
    }

    @Test
    void getDetailGuestBooking() {
        assertNotNull(guestController.getDetailGuestBooking(1));
    }

    @Test
    void getHostsList() throws ParseException {
        assertNotNull(guestController.getHostsList("alias"));
    }

    @Test
    void addReview() {
        assertNotNull(guestController.addReview(new AddReviewDTO()));
    }

    @Test
    void updateReview() {
        assertNotNull(guestController.updateReview(new UpdateReviewDTO(1,2, "prueba")));
    }

    @Test
    void addQualifyToHost() {
        assertNotNull(guestController.addQualifyToHost(new QualifyUserDTO()));
    }

    @Test
    void deleteHostQualification() {
        assertNotNull(guestController.deleteHostQualification("host", "guest"));
    }

    @Test
    void getGuestBookingsAllInfo() {
        List<GuestBookingInfoDetailResponse> responses = new ArrayList<>();
        when(bookingService.getGuestBookingsWithAllInfo("guest")).thenReturn(responses);
        assertNotNull(guestController.getGuestBookingsAllInfo("guest"));
    }

    @Test
    void addOrDeleteFavorites() {
        assertNotNull(guestController.addOrDeleteFavorites(new FavoriteDTO()));
    }

    @Test
    void getFavorites() {
        assertNotNull(guestController.getFavorites("alias"));
    }
}