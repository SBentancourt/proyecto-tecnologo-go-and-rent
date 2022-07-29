package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class HostBookingsPaymentsDTO {
    private String hostAlias;
    private int bookingId;
    private Float finalPrice;
    private String startDate;
    private String account;
    private String bank;
}
