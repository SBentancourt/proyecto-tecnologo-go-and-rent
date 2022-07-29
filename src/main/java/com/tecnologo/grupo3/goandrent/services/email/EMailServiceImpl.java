package com.tecnologo.grupo3.goandrent.services.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.exceptions.AccommodationException;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.UserRepository;
import com.tecnologo.grupo3.goandrent.services.BookingService;
import com.tecnologo.grupo3.goandrent.services.UserService;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class EMailServiceImpl implements EMailService {

    private final static String NO_RESPONSE_EMAIL = "no-responder@go-and-rent.com";

    @Autowired
    private AmazonSimpleEmailServiceClient client;

    //@Autowired
    //private TemplateEngine templateEngine;

    @Autowired
    private ITemplateEngine templateEngine;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendEmailHostSignUp(Set<String> emailsAdmins, String aliasHost, String emailHost, String nameAccommodation, Integer idAccommodation) {
        Destination destination = new Destination().withToAddresses(emailsAdmins);
        Context context = new Context();

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("alias", aliasHost);  emailData.put("email", emailHost);
        emailData.put("nameAcc", nameAccommodation);    emailData.put("idAcc", idAccommodation);
        context.setVariables(emailData);

        String html = templateEngine.process("signupHostEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Se ha registrado un nuevo usuario Anfitrión"))
                .withBody(body);

        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);
        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailNewBooking(int idBooking) {
        // -- se obtienen los datos asociados a la reserva (aquellos que quiero mostrar en el email)
        Booking booking = bookingService.getBookingObject(idBooking);
        String guestEmail = booking.getGuest().getEmail();
        String guestAlias = booking.getGuest().getAlias();
        String accommodationName = booking.getAccommodation().getName();
        String hostEmail = booking.getAccommodation().getHost().getEmail();
        String hostName = booking.getAccommodation().getHost().getName();
        Date start_date = booking.getStartDate();
        Date end_date = booking.getEndDate();

        // -- A partir de aca genero el email
        Destination destination = new Destination().withToAddresses(hostEmail);
        Context context = new Context();

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("guestAlias", guestAlias);        emailData.put("guestEmail", guestEmail);
        emailData.put("nameAcc", accommodationName);    emailData.put("hostName", hostName);
        emailData.put("startDate", start_date);         emailData.put("endDate", end_date);
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("newBookingEmail", context);

        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);

        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Se ha realizado una reserva en uno de sus alojamientos"))
                .withBody(body);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);

        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailConfirmBooking(int idBooking) {
        // -- se obtienen los datos asociados a la reserva (aquellos que quiero mostrar en el email)
        Booking booking = bookingService.getBookingObject(idBooking);
        String guestEmail = booking.getGuest().getEmail();
        String guestName = booking.getGuest().getName();
        String accommodationName = booking.getAccommodation().getName();
        String hostEmail = booking.getAccommodation().getHost().getEmail();
        String hostName = booking.getAccommodation().getHost().getName();
        String hostLastName = booking.getAccommodation().getHost().getLastName();
        String hostFullName = hostName.trim() + " " + hostLastName.trim();
        String hostPhone = booking.getAccommodation().getHost().getPhone();
        Date start_date = booking.getStartDate();
        Date end_date = booking.getEndDate();
        float total = booking.getFinalPrice();

        // -- A partir de aca genero el email
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("hostPhone", hostPhone);          emailData.put("hostEmail", hostEmail);
        emailData.put("nameAcc", accommodationName);    emailData.put("hostFullName", hostFullName);
        emailData.put("startDate", start_date);         emailData.put("endDate", end_date);
        emailData.put("guestName", guestName);          emailData.put("idBooking", idBooking);
        emailData.put("total", total);
        Context context = new Context();
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("confirmBookingEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Confirmación de reserva de alojamiento!"))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(guestEmail);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);
        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailRejectBooking(int idBooking) {
        // -- se obtienen los datos asociados a la reserva (aquellos que quiero mostrar en el email)
        Booking booking = bookingService.getBookingObject(idBooking);
        String guestEmail = booking.getGuest().getEmail();
        String guestName = booking.getGuest().getName();
        String accommodationName = booking.getAccommodation().getName();
        String hostEmail = booking.getAccommodation().getHost().getEmail();
        Date start_date = booking.getStartDate();
        Date end_date = booking.getEndDate();
        float total = booking.getFinalPrice();

        // -- A partir de aca genero el email
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("hostEmail", hostEmail);          emailData.put("nameAcc", accommodationName);
        emailData.put("startDate", start_date);         emailData.put("endDate", end_date);
        emailData.put("guestName", guestName);          emailData.put("idBooking", idBooking);
        emailData.put("total", total);
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("rejectBookingEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Rechazo de reserva de alojamiento!"))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(guestEmail);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);

        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailRefundBookingHost(int idBooking) {
        Booking booking = bookingService.getBookingObject(idBooking);
        String guestEmail = booking.getGuest().getEmail();
        String guestName = booking.getGuest().getName();
        String accommodationName = booking.getAccommodation().getName();
        String hostEmail = booking.getAccommodation().getHost().getEmail();
        Date start_date = booking.getStartDate();
        Date end_date = booking.getEndDate();
        float total = booking.getFinalPrice();

        // -- A partir de aca genero el email
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("hostEmail", hostEmail);          emailData.put("nameAcc", accommodationName);
        emailData.put("startDate", start_date);         emailData.put("endDate", end_date);
        emailData.put("guestName", guestName);          emailData.put("idBooking", idBooking);
        emailData.put("total", total);
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("refundBookingEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Cancelación de reserva de alojamiento!"))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(guestEmail);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);
        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailRefundBookingGuest(int idBooking) {
        Booking booking = bookingService.getBookingObject(idBooking);
        String hostEmail = booking.getAccommodation().getHost().getEmail();
        String guestEmail = booking.getGuest().getEmail();
        String guestAlias = booking.getGuest().getAlias();
        String accommodationName = booking.getAccommodation().getName();
        String hostName = booking.getAccommodation().getHost().getName();
        Date start_date = booking.getStartDate();
        Date end_date = booking.getEndDate();
        float total = booking.getFinalPrice();

        // -- A partir de aca genero el email
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("guestEmail", guestEmail);        emailData.put("nameAcc", accommodationName);
        emailData.put("startDate", start_date);         emailData.put("endDate", end_date);
        emailData.put("hostName", hostName);            emailData.put("idBooking", idBooking);
        emailData.put("total", total);                  emailData.put("guestAlias", guestAlias);
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("refundGuestBookingEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Cancelación de reserva de alojamiento!"))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(hostEmail);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);

        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailRecoveryPassword(String email, String name, int code) {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", name);        emailData.put("code", code);
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("recoverPasswordEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Código de recuperación de contraseña"))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(email);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);

        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailStatusAccount(String email, String userName, UserStatus status) {
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("userName", userName);
        String bodyMessage;
        String statusString;
        if (status == UserStatus.BLOQUEADO) {
            bodyMessage = "Tu cuenta ha quedado bloqueada por tiempo indefinido.";
            statusString = "Bloqueado";
        }
        else {
            bodyMessage = "Tu cuenta ha sido desbloqueada!! Te esperamos nuevamente en Go&Rent.";
            statusString = "Desbloqueado";
        }
        emailData.put("bodyMessage", bodyMessage);
        context.setVariables(emailData);

        String html = templateEngine.process("changeStatusAccount", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Usuario " + statusString))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(email);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);

        client.sendEmail(emailRequest);
    }

    @Override
    public void sendEmailRejectHost(String alias) {
        User user = userRepository.findUserByAlias(alias).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe usuario con alias: " + alias));
        String hostEmail = user.getEmail();
        Context context = new Context();
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("name", user.getName());        emailData.put("alias", alias);
        context.setVariables(emailData);

        // -- aca se indica a que template se le van a pasar los datos
        String html = templateEngine.process("rejectSignupHostEmail", context);
        Content htmlContent = new Content().withData(html);
        Content textContent = new Content().withData("Version en texto");
        Body body = new Body().withHtml(htmlContent).withText(textContent);
        Message message = new Message()
                .withSubject(new Content("IMPORTANTE: Rechazo de registro de cuenta"))
                .withBody(body);

        Destination destination = new Destination().withToAddresses(hostEmail);
        SendEmailRequest emailRequest = new SendEmailRequest().withSource(NO_RESPONSE_EMAIL).withDestination(destination)
                .withMessage(message);

        client.sendEmail(emailRequest);
    }
}
