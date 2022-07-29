package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.AdminSignupBodyDTO;
import com.tecnologo.grupo3.goandrent.dtos.HostBookingsPaymentsDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.StatisticsResponse;
import com.tecnologo.grupo3.goandrent.entities.users_types.Admin;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminService {
    Optional<Admin> getAdminByEmail(String email);
    Set<String> getAdminsEmails();
    void saveAdmin(AdminSignupBodyDTO adminDTO) throws ParseException;
    StatisticsResponse getStatistics();
    List<HostBookingsPaymentsDTO> getHostsPaymentsMonthYear(int month, int year) throws ParseException;
}
