package com.tecnologo.grupo3.goandrent.dtos.responses;

import com.tecnologo.grupo3.goandrent.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class StatisticsResponse {
    private List<BookingsByRegionDTO> bookingsByRegion;
    private List<RegisteredUserPerMonthDTO> registeredUsersPerMonth;
    private List<AccommodationsByQualificationDTO> accommodationsQualification;
    private List<AccommodationsByRegionDTO> accommodationsByRegion;
    private List<AccommodatiosRegisteredByMonthDTO> accommodatiosRegisteredPerMonth;
    private List<HostConfirmedRefusedDTO> hostConfirmedRefusedList;
}
