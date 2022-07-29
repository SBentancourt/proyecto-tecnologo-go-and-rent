package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.responses.HostWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface HostService {
    void saveHost(MultiValueMap<String, String> info, MultipartFile[] imagenes) throws ParseException;
    Optional<Host> getHostByEmail(String email);
    void updateAccommodationStatus(String aliasHost, AccommodationStatus status);
    Boolean existHostByAlias(String alias);
    void addAccommodation(MultiValueMap<String, String> info, MultipartFile[] imagenes, String aliasHost, Boolean newHost);
    List<HostWithBookingsResponse> getHostWithBookingByGuestAlias(String alias) throws ParseException;
}
