package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.dtos.HostAccommodationInfoDTO;
import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.GuestWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.HostBookingsListResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.HostBookingsResponse;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HostControllerTest {

    @Mock
    private HostService hostService;
    @Mock
    private AccommodationService accommodationService;
    @Mock
    private BookingService bookingService;
    @Mock
    private GuestService guestService;
    @Mock
    private UserService userService;

    @InjectMocks
    private HostController hostController;

    private MultiValueMap<String, String> info = new LinkedMultiValueMap<String, String>();
    private MultipartFile[] imagenes = new MultipartFile[1];
    private HostAccommodationInfoDTO hostAccommodationInfoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addAccommodation() {
        when(hostService.existHostByAlias("alias")).thenReturn(true);
        assertNotNull(hostController.addAccommodation("alias", info, imagenes));
    }
    @Test
    void addAccommodation_HOST_NoExist() {
        when(hostService.existHostByAlias("alias")).thenReturn(false);
        assertNotNull(hostController.addAccommodation("alias", info, imagenes));
    }

    @Test
    void deleteAccommodation() throws ParseException {
        assertNotNull(hostController.deleteAccommodation(1));
    }

    @Test
    void accommodationList() {
        List<HostAccommodationInfoDTO> list = new ArrayList<>();
        when(accommodationService.getAccommodationsByHost("alias")).thenReturn(list);
        assertNotNull(hostController.accommodationList("alias"));
    }

    @Test
    void testAccommodationList() {
        assertNotNull(hostController.updateAccommodation(1, info, imagenes));
    }

    @Test
    void hostBookings() throws ParseException {
        List<HostBookingsResponse> list = new ArrayList<>();
        HostBookingsListResponse hostBookingsListResponse = new HostBookingsListResponse(list, 1,1,1,1,true);
        when(bookingService.getHostBookings("alias", info, 1,1)).thenReturn(hostBookingsListResponse);
        assertNotNull(hostController.hostBookings("alias", info, 1, 1));
    }

    @Test
    void getListGuests() throws ParseException {
        List<GuestWithBookingsResponse> list = new ArrayList<>();
        when(guestService.getGuestsWithBookingsByHostAlias("alias")).thenReturn(list);
        assertNotNull(hostController.getListGuests("alias"));
    }

    @Test
    void addQualifyToGuest() {
        assertNotNull(hostController.addQualifyToGuest(new QualifyUserDTO()));
    }

    @Test
    void deleteGuestQualification() {
        assertNotNull(hostController.deleteGuestQualification("host", "guest"));
    }

    @Test
    void getReviewsAccommodation() {
        assertNotNull(hostController.getReviewsAccommodation(1));
    }
}