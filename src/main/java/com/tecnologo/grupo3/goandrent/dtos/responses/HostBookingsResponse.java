package com.tecnologo.grupo3.goandrent.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class HostBookingsResponse {
    private int bookingId;
    private int accommodationId;
    private String accommodationName;
    private String startDate;
    private String endDate;
    private String bookingStatus;
    private String paymentStatus;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestAlias;
    private int guestQualification;
}
