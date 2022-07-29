package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.AddReviewDTO;
import com.tecnologo.grupo3.goandrent.dtos.BookingDataDTO;
import com.tecnologo.grupo3.goandrent.dtos.VerifyBookingDTO;
import com.tecnologo.grupo3.goandrent.entities.*;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.BookingRepository;
import com.tecnologo.grupo3.goandrent.services.AccommodationService;
import com.tecnologo.grupo3.goandrent.services.GuestService;
import com.tecnologo.grupo3.goandrent.services.ReviewService;
import com.tecnologo.grupo3.goandrent.services.UserCalificationService;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private AccommodationService accommodationService;
    @Mock
    private GuestService guestService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private UserCalificationService userCalificationService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private VerifyBookingDTO verifyBookingDTO;
    private BookingDataDTO bookingDataDTO;
    private Accommodation accommodation;
    private Guest guest;
    private Booking booking;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);
        verifyBookingDTO = new VerifyBookingDTO();
        verifyBookingDTO.setEnd_date("01/01/2020"); verifyBookingDTO.setStart_date("01/01/2020"); verifyBookingDTO.setId_accommodation(1);
        bookingDataDTO = new BookingDataDTO();
        bookingDataDTO.setEnd_date("02/01/2020"); bookingDataDTO.setStart_date("01/01/2020"); bookingDataDTO.setIdAccommodation(0); bookingDataDTO.setAliasGuest("guest");
        Set<Gallery> gallerySet = new HashSet<>();
        accommodation = new Accommodation("prueba", "prueba", AccommodationStatus.ACTIVO, Float.parseFloat("10"), new Date(), new Location(), gallerySet, new Host());
        guest = new Guest("guest", "guest", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "0000", new Date(),"");
        booking = new Booking(new Date(), new Date(), BookingStatus.PENDIENTE, PaymentStatus.PENDIENTE, Float.parseFloat("10"), accommodation, guest);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = format.parse(bookingDataDTO.getStart_date());
        Date endDate = format.parse(bookingDataDTO.getEnd_date());
        booking = new Booking(startDate, endDate, BookingStatus.PENDIENTE, PaymentStatus.PENDIENTE, Float.parseFloat("10"), accommodation, guest);
    }

    @Test
    void existBookingsDateRange() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = format.parse(verifyBookingDTO.getStart_date());
        Date endDate = format.parse(verifyBookingDTO.getEnd_date());
        when(bookingRepository.existsBookingsInDateRange(1, startDate, endDate, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(true);
        assertTrue(bookingService.existBookingsDateRange(verifyBookingDTO));
    }

    @Test
    void generateBooking() throws ParseException {

        when(accommodationService.getAccommodationObject(0)).thenReturn(accommodation);
        when(guestService.getGuestByAlias("guest")).thenReturn(Optional.of(guest));
        when(bookingRepository.save(booking)).thenReturn(booking);
        assertTrue(bookingService.generateBooking(bookingDataDTO) == 0);
    }

    @Test
    void getBookingObject() {
        when(bookingRepository.getById(0)).thenReturn(booking);
        assertNotNull(bookingService.getBookingObject(0));
    }

    @Test
    void updateBooking() {
        bookingService.updateBooking(booking);
    }

    @Test
    void getFinalPriceBooking() throws ParseException {
        when(accommodationService.getAccommodationObject(0)).thenReturn(accommodation);
        assertNotNull(bookingService.getFinalPriceBooking(0, "01/01/2020", "02/01/2020"));
    }

    @Test
    void getAuthorizationIdPayment() {
        PaypalPayment payment = new PaypalPayment("orden1", "autorizacion", "confirmacion", 1);
        booking.setPaypalPayment(payment);
        when(bookingRepository.getById(0)).thenReturn(booking);
        assertNotNull(bookingService.getAuthorizationIdPayment(0));

    }

    @Test
    void getTotalPriceBooking() {
        when(bookingRepository.getById(0)).thenReturn(booking);
        assertNotNull(bookingService.getTotalPriceBooking(0));
    }

    @Test
    void updatePaymentStatus() {
        when(bookingRepository.getById(0)).thenReturn(booking);
        bookingService.updatePaymentStatus(0, PaymentStatus.COMPLETADO);
    }

    @Test
    void updateBookingStatus() {
        when(bookingRepository.getById(0)).thenReturn(booking);
        bookingService.updateBookingStatus(0, BookingStatus.ACEPTADA);
    }

    @Test
    void getOrderId() {
        PaypalPayment payment = new PaypalPayment("orden1", "autorizacion", "confirmacion", 1);
        booking.setPaypalPayment(payment);
        when(bookingRepository.getById(0)).thenReturn(booking);
        assertNotNull(bookingService.getOrderId(0));
    }

    @Test
    void guestWithBookingsInProgress() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        when(bookingRepository.guestWithBookingsInProgress("guest", today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(true);
        assertTrue(bookingService.guestWithBookingsInProgress("guest"));
    }

    @Test
    void hostWithBookingsInProgress() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        when(bookingRepository.hostWithBookingsInProgress("host", today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(true);
        assertTrue(bookingService.hostWithBookingsInProgress("host"));
    }

    @Test
    void getBookingsInfoAndReviewsByAccommodationId() {
        Review review = new Review(1, "descripcion", new Date());
        booking.setReview(review);
        when(bookingRepository.findBookingsByAccommodation_Id(0)).thenReturn(Arrays.asList(booking));
        assertNotNull(bookingService.getBookingsInfoAndReviewsByAccommodationId(0));
    }

    @Test
    void accommodationWithBookings() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        when(bookingRepository.accommodationWithBookings(0, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(true);
        assertTrue(bookingService.accommodationWithBookings(0));
    }

    @Test
    void getHostBookings() throws ParseException {
        MultiValueMap<String, String> filtros = new LinkedMultiValueMap<String, String>();
        filtros.set("acc_name", "prueba");
        filtros.set("booking_status", "PENDIENTE");
        filtros.set("start_date","01/01/1990");
        filtros.set("end_date", "01/01/2030");

        when(accommodationService.getAccommodationsByHostAlias("host")).thenReturn(Arrays.asList(accommodation));
        when(bookingRepository.findBookingsByAccommodation_Id(0)).thenReturn(Arrays.asList(booking));
        when(userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers("host", "guest")).thenReturn(1);
        assertNotNull(bookingService.getHostBookings("host", filtros, 0, 5));
    }

    @Test
    void getGuestBookings() {
        when(bookingRepository.guestBookingsStartDateOrderDesc("guest")).thenReturn(Arrays.asList(booking));
        assertNotNull(bookingService.getGuestBookings("guest"));
    }

    @Test
    void getBookingDetailByBookingId() {
        Review review = new Review(1, "descripcion", new Date());
        booking.setReview(review);
        PaypalPayment payment = new PaypalPayment("orden1", "autorizacion", "confirmacion", 1);
        booking.setPaypalPayment(payment);
        when(bookingRepository.getById(0)).thenReturn(booking);
        assertNotNull(bookingService.getBookingDetailByBookingId(0));
    }

    @Test
    void addBookingReview() {
        when(bookingRepository.getById(0)).thenReturn(booking);
        AddReviewDTO addReviewDTO = new AddReviewDTO();
        addReviewDTO.setBookingId(0);
        addReviewDTO.setDescription("test");
        addReviewDTO.setQualification(3);
        bookingService.addBookingReview(addReviewDTO);
    }

    @Test
    void getGuestBookingsWithAllInfo() {
        when(bookingRepository.guestBookingsStartDateOrderDesc("guest")).thenReturn(Arrays.asList(booking));
        when(userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers("host", "guest")).thenReturn(1);
        assertNotNull(bookingService.getGuestBookingsWithAllInfo("guest"));
    }

    @Test
    void getAliasGuestByBookingId() {
        when(bookingRepository.getById(0)).thenReturn(booking);
        assertNotNull(bookingService.getAliasGuestByBookingId(0));
    }

    @Test
    void getReviewsByAccommodationId() {
        Review review = new Review(1, "desc", new Date());
        booking.setReview(review);
        when(bookingRepository.findBookingsByAccommodation_Id(0)).thenReturn(Arrays.asList(booking));
        assertNotNull(bookingService.getReviewsByAccommodationId(0));
    }
}