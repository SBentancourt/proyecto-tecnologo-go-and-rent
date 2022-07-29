package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.PaypalPayment;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.PaypalPaymentRepository;
import com.tecnologo.grupo3.goandrent.services.BookingService;
import com.tecnologo.grupo3.goandrent.utils.enums.Bank;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PaypalPaymentServiceImplTest {

    @Mock
    private PaypalPaymentRepository paypalPaymentRepository;
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private PaypalPaymentServiceImpl paypalPaymentService;

    private PaypalPayment paypalPayment;
    private Booking booking;
    private Accommodation accommodation;
    private Host host;
    private Guest guest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paypalPayment = new PaypalPayment("1", "prueba", "prueba", 1);
        host = new Host("alia", "email", "password", "name", "lastName", UserStatus.ACEPTADO, new Date(), "0000", new Date(), "", Bank.SCOTIABANK, "account");
        accommodation = new Accommodation();
        guest = new Guest();
        booking = new Booking(new Date(), new Date(), BookingStatus.PENDIENTE, PaymentStatus.PENDIENTE, Float.parseFloat("10"), accommodation, guest);
    }

    @Test
    void savePaypalPayment() {
        when(paypalPaymentRepository.save(paypalPayment)).thenReturn(paypalPayment);
        when(bookingService.getBookingObject(1)).thenReturn(booking);
        paypalPaymentService.savePaypalPayment("1", "prueba", "prueba", 1, 1);
    }

    @Test
    void getLastInvoiceId() {
        when(paypalPaymentRepository.getLastInvoiceId()).thenReturn(1);
        assertNotNull(paypalPaymentService.getLastInvoiceId());
    }

    @Test
    void updateConfirmationIdInvoiceId() {
        when(paypalPaymentRepository.getById("1")).thenReturn(paypalPayment);
        paypalPaymentService.updateConfirmationIdInvoiceId("1", "prueba", 1);
    }

    @Test
    void getConfirmationId() {
        when(paypalPaymentRepository.getById("1")).thenReturn(paypalPayment);
        assertNotNull(paypalPaymentService.getConfirmationId("1"));
    }

    @Test
    void getInvoiceId() {
        when(paypalPaymentRepository.getById("1")).thenReturn(paypalPayment);
        assertNotNull(paypalPaymentService.getInvoiceId("1"));
    }
}