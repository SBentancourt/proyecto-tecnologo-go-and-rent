package com.tecnologo.grupo3.goandrent.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class HostWithBookingsResponse {
    private String alias;
    private String name;
    private String lastName;
    private int lastBookingId;
    private String lastAccommodationName;
    private String startDate;
    private String endDate;
}
