package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.AddReviewDTO;
import com.tecnologo.grupo3.goandrent.dtos.BookingDataDTO;
import com.tecnologo.grupo3.goandrent.dtos.ReviewInfoDTO;
import com.tecnologo.grupo3.goandrent.dtos.VerifyBookingDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.*;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;
import java.util.List;

public interface BookingService {
    Boolean existBookingsDateRange(VerifyBookingDTO verifyBookingDTO) throws ParseException;
    int generateBooking(BookingDataDTO bookingDataDTO) throws ParseException;
    Booking getBookingObject(int id);
    void updateBooking(Booking booking);
    Double getFinalPriceBooking(int idAccommodation, String startDate, String endDate) throws ParseException;
    String getAuthorizationIdPayment(int id);
    Float getTotalPriceBooking(int idBooking);
    void updatePaymentStatus(int idBooking, PaymentStatus status);
    void updateBookingStatus(int idBooking, BookingStatus status);
    String getOrderId(int idBooking);
    Boolean guestWithBookingsInProgress(String aliasGuest) throws ParseException;
    Boolean hostWithBookingsInProgress(String aliasHost) throws ParseException;
    List<ReviewInfoDTO> getBookingsInfoAndReviewsByAccommodationId(int id);
    Boolean accommodationWithBookings(int id) throws ParseException;
    HostBookingsListResponse getHostBookings(String alias, MultiValueMap<String, String> filtros, int nroPag, int cantReg) throws ParseException;
    List<GuestBookingsInfoResponse> getGuestBookings(String alias);
    GuestBookingDetailResponse getBookingDetailByBookingId(int id);
    void addBookingReview(AddReviewDTO reviewDTO);
    List<GuestBookingInfoDetailResponse> getGuestBookingsWithAllInfo(String alias);
    String getAliasGuestByBookingId(int id);
    List<ReviewResponse> getReviewsByAccommodationId(int id);
}
