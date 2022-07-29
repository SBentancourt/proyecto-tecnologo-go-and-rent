package com.tecnologo.grupo3.goandrent.controllers;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import com.tecnologo.grupo3.goandrent.dtos.BookingDataDTO;
import com.tecnologo.grupo3.goandrent.dtos.BookingIdDTO;
import com.tecnologo.grupo3.goandrent.services.BookingService;
import com.tecnologo.grupo3.goandrent.services.PaypalPaymentService;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.services.paypal.PaypalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookingControllerTest {

    @Mock
    private PaypalServiceImpl paypalService;
    @Mock
    private BookingService bookingService;
    @Mock
    private PaypalPaymentService paypalPaymentService;
    @Mock
    private EMailService eMailService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDataDTO bookingDTO = new BookingDataDTO();
    private Payment payment = new Payment();
    private BookingIdDTO bookingIdDTO = new BookingIdDTO();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingDTO.setAliasGuest("guest");
        bookingDTO.setIdAccommodation(1);
        bookingDTO.setStart_date("10/10/2020");
        bookingDTO.setEnd_date("11/10/2020");

        Payer payer = new Payer();
        Links links = new Links("prueba", "approval_url");
        payment = new Payment("authorize", payer);
        payment.setLinks(Arrays.asList(links));
        Transaction transaction = new Transaction();
        transaction.setDescription("description:1");
        RelatedResources relatedResources = new RelatedResources();
        Authorization authorization = new Authorization();
        relatedResources.setAuthorization(authorization);
        transaction.setRelatedResources(Arrays.asList(relatedResources));
        payment.setTransactions(Arrays.asList(transaction));
        payment.setState("approved");

        bookingIdDTO.setBooking_id(1);
    }

    @Test
    void confirmAccommodationBooking() throws ParseException, PayPalRESTException {
        when(bookingService.generateBooking(bookingDTO)).thenReturn(1);
        when(bookingService.getFinalPriceBooking(1, "10/10/2020", "11/10/2020")).thenReturn(Double.valueOf("10"));
        when(paypalService.createPayment(Double.valueOf("10"), "USD", "paypal",
                "authorize", "reserva número:" + 1, "https://api.go-and-rent.com/prod-1/" + "booking/pay/cancel",
                "https://api.go-and-rent.com/prod-1/" + "booking/pay/success")).thenReturn(payment);
        assertNotNull(bookingController.confirmAccommodationBooking(bookingDTO));
    }

    /*@Test
    void confirmPendingBooking() {
        when(bookingService.getAuthorizationIdPayment(1)).thenReturn("authorization");
        when(bookingService.getTotalPriceBooking(1)).thenReturn(Float.valueOf("10"));
        when(paypalPaymentService.getLastInvoiceId()).thenReturn(2);
        when(bookingService.getOrderId(1)).thenReturn("order1");
        assertNotNull(bookingController.confirmPendingBooking(bookingIdDTO));
    }*/

    @Test
    void rejectPendingBooking() {
    }

    @Test
    void refundBooking() {
    }

    @Test
    void successPay() throws PayPalRESTException {
        when(paypalService.executePayment("paymentid", "payerid")).thenReturn(payment);
        //when(payment.getTransactions().get(0).getDescription()).thenReturn("description");
        assertNotNull(bookingController.successPay("paymentid", "token", "payerid"));
    }

    @Test
    void cancelPay() {
    }
}