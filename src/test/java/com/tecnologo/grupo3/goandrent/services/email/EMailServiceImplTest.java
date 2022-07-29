package com.tecnologo.grupo3.goandrent.services.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.entities.Location;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.UserRepository;
import com.tecnologo.grupo3.goandrent.services.BookingService;
import com.tecnologo.grupo3.goandrent.utils.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EMailServiceImplTest {

    @Mock
    private AmazonSimpleEmailServiceClient client;
    @Mock
    private ITemplateEngine templateEngine;
    @Mock
    private BookingService bookingService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EMailServiceImpl eMailService;

    private Booking booking;
    private Host host;
    private Guest guest;
    private Accommodation accommodation;
    private String html = "html";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        host = new Host("host", "host@test.com", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "0000", new Date(), "", Bank.ITAU, "0123");
        guest = new Guest("guest", "guest@test.com", "password", "name", "lastname", UserStatus.ACEPTADO, new Date(), "0000", new Date(), "");
        Set<Gallery> galleries = new HashSet<>();
        accommodation = new Accommodation("name", "description", AccommodationStatus.ACTIVO, Float.parseFloat("10"), new Date(), new Location(), galleries, host);
        booking = new Booking(new Date(), new Date(), BookingStatus.ACEPTADA, PaymentStatus.COMPLETADO, Float.parseFloat("10"), accommodation, guest);
    }

    @Test
    void sendEmailHostSignUp() {
        Set<String> emailsAdmins = new HashSet<>();
        emailsAdmins.add("admin@test.com");
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("alias", "host");  emailData.put("email", "host@test.com");
        emailData.put("nameAcc", "prueba");    emailData.put("idAcc", 1);
        context.setVariables(emailData);
        when(templateEngine.process("signupHostEmail", context)).thenReturn(html);
        eMailService.sendEmailHostSignUp(emailsAdmins, "host", "host@test.com", "prueba", 1);
    }

    @Test
    void sendEmailNewBooking() {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("guestAlias", "guest");        emailData.put("guestEmail", "guest@test.com");
        emailData.put("nameAcc", "name");    emailData.put("hostName", "name");
        emailData.put("startDate", "05/06/2022");         emailData.put("endDate", "05/06/2022");
        context.setVariables(emailData);
        when(templateEngine.process("newBookingEmail", context)).thenReturn(html);
        when(bookingService.getBookingObject(0)).thenReturn(booking);
        eMailService.sendEmailNewBooking(0);
    }

    @Test
    void sendEmailConfirmBooking() {
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("hostPhone", "0000");          emailData.put("hostEmail", "host@test.com");
        emailData.put("nameAcc", "name");    emailData.put("hostFullName", "name lastname");
        emailData.put("startDate", "05/06/2022");         emailData.put("endDate", "05/06/2022");
        emailData.put("guestName", "name");          emailData.put("idBooking", 0);
        emailData.put("total", 10);
        Context context = new Context();
        context.setVariables(emailData);
        when(templateEngine.process("confirmBookingEmail", context)).thenReturn(html);
        when(bookingService.getBookingObject(0)).thenReturn(booking);
        eMailService.sendEmailConfirmBooking(0);
    }

    @Test
    void sendEmailRejectBooking() {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("hostEmail", "host@test.com");          emailData.put("nameAcc", "name");
        emailData.put("startDate", "05/06/2022");         emailData.put("endDate", "05/06/2022");
        emailData.put("guestName", "name");          emailData.put("idBooking", 0);
        emailData.put("total", 10);
        context.setVariables(emailData);
        when(templateEngine.process("rejectBookingEmail", context)).thenReturn(html);
        when(bookingService.getBookingObject(0)).thenReturn(booking);
        eMailService.sendEmailRejectBooking(0);
    }

    @Test
    void sendEmailRefundBookingHost() {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("hostEmail", "host@test.com");          emailData.put("nameAcc", "name");
        emailData.put("startDate", "05/06/2022");         emailData.put("endDate", "05/06/2022");
        emailData.put("guestName", "name");          emailData.put("idBooking", 0);
        emailData.put("total", 10);
        context.setVariables(emailData);
        when(templateEngine.process("refundBookingEmail", context)).thenReturn(html);
        when(bookingService.getBookingObject(0)).thenReturn(booking);
        eMailService.sendEmailRefundBookingHost(0);
    }

    @Test
    void sendEmailRefundBookingGuest() {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("guestEmail", "guest@test.com");        emailData.put("nameAcc", "name");
        emailData.put("startDate", "05/06/2022");         emailData.put("endDate", "05/06/2022");
        emailData.put("hostName", "name");            emailData.put("idBooking", 0);
        emailData.put("total", 10);                  emailData.put("guestAlias", "guest");
        context.setVariables(emailData);
        when(templateEngine.process("refundGuestBookingEmail", context)).thenReturn(html);
        when(bookingService.getBookingObject(0)).thenReturn(booking);
        eMailService.sendEmailRefundBookingGuest(0);
    }

    @Test
    void sendEmailRecoveryPassword() {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", "name");        emailData.put("code", 123456);
        context.setVariables(emailData);
        when(templateEngine.process("recoverPasswordEmail", context)).thenReturn(html);
        eMailService.sendEmailRecoveryPassword("user@test.com", "name", 123456);
    }

    @Test
    void sendEmailStatusAccount() {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("userName", "name");
        emailData.put("bodyMessage", "mensaje");
        context.setVariables(emailData);
        when(templateEngine.process("changeStatusAccount", context)).thenReturn(html);
        eMailService.sendEmailStatusAccount("user@test.com", "name", UserStatus.ACEPTADO);
    }

    @Test
    void sendEmailRejectHost() {
        when(userRepository.findUserByAlias("guest")).thenReturn(Optional.of(guest));
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", "name");        emailData.put("alias", "guest");
        context.setVariables(emailData);
        when(templateEngine.process("rejectSignupHostEmail", context)).thenReturn(html);
        eMailService.sendEmailRejectHost("guest");
    }
}