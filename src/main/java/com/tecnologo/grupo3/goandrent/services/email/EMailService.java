package com.tecnologo.grupo3.goandrent.services.email;

import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;

import java.util.Set;

public interface EMailService {
    void sendEmailHostSignUp(Set<String> emailsAdmins, String aliasHost, String emailHost, String nameAccommodation, Integer idAccommodation);
    void sendEmailNewBooking(int idBooking);
    void sendEmailConfirmBooking(int idBooking);
    void sendEmailRejectBooking(int idBooking);
    void sendEmailRefundBookingHost(int idBooking);
    void sendEmailRefundBookingGuest(int idBooking);
    void sendEmailRecoveryPassword(String email, String name, int code);
    void sendEmailStatusAccount(String email, String userName, UserStatus status);
    void sendEmailRejectHost(String alias);
}
