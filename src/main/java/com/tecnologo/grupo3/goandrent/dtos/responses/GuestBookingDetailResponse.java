package com.tecnologo.grupo3.goandrent.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class GuestBookingDetailResponse {
    private int bookingId;
    private int accommodationId;
    private String accommodationName;
    private String startDate;
    private String endDate;
    private String bookingStatus;
    private String paymentStatus;
    private String hostName;
    private String hostEmail;
    private Float finalPrice;
    private int reviewId;
    private String reviewDesc;
    private int reviewQualification;
    private String accommodationPhoto;
    private String hostAlias;
    private int hostQualification;
}
