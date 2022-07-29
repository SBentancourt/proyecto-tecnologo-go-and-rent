package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.*;
import com.tecnologo.grupo3.goandrent.dtos.responses.StatisticsResponse;
import com.tecnologo.grupo3.goandrent.entities.users_types.Admin;
import com.tecnologo.grupo3.goandrent.repositories.AdminRepository;
import com.tecnologo.grupo3.goandrent.services.AdminService;
import com.tecnologo.grupo3.goandrent.utils.enums.Bank;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    @Override
    public Set<String> getAdminsEmails() {
        List<Admin> admins = adminRepository.findAll();
        Set<String> adminsEmails = new HashSet<>();
        for (Admin a: admins){
            adminsEmails.add(a.getEmail());
        }
        return adminsEmails;
    }

    @Override
    public void saveAdmin(AdminSignupBodyDTO adminDTO) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date birthday = format.parse(adminDTO.getBirthday());

        Admin newAdmin = adminRepository.save(new Admin(adminDTO.getAlias(), adminDTO.getEmail(), adminDTO.getPassword(),
                adminDTO.getName(), adminDTO.getLastName(), UserStatus.ACEPTADO, new Date(),
                adminDTO.getPhone(), birthday, adminDTO.getPicture()));
    }

    @Override
    public StatisticsResponse getStatistics() {

        // -- 1° Cantidad de reservas por región y por mes
        List<Object[]> respuestaQuery = adminRepository.cantidadReservasPorRegionMes(0);
        List<BookingsByRegionDTO> bookingsByRegionList = new ArrayList<>();
        for (Object[] resp: respuestaQuery){
            bookingsByRegionList.add(new BookingsByRegionDTO(Integer.parseInt(resp[0].toString()),Integer.parseInt(resp[1].toString()),Integer.parseInt(resp[2].toString()),
                                                                resp[3].toString(),resp[4].toString(),resp[5].toString()));
        }
        // -- 2° Cantidad de usuarios registrados por mes (huéspedes y anfitriones)
        respuestaQuery = adminRepository.cantidadUsuariosRegistradosPorMes();
        List<RegisteredUserPerMonthDTO> registeredUserPerMonthList = new ArrayList<>();
        for (Object[] resp: respuestaQuery){
            registeredUserPerMonthList.add(new RegisteredUserPerMonthDTO(Integer.parseInt(resp[0].toString()),resp[1].toString(),Integer.parseInt(resp[2].toString()),Integer.parseInt(resp[3].toString())));
        }
        // -- 3° Cantidad de alojamientos por calificación
        List<AccommodationsByQualificationDTO> accommodationsByQualificationList = new ArrayList<>();
        respuestaQuery = adminRepository.cantidadAlojamientosPorCalificacion();
        for (Object[] resp: respuestaQuery){
            accommodationsByQualificationList.add(new AccommodationsByQualificationDTO(Integer.parseInt(resp[0].toString()),Integer.parseInt(resp[1].toString())));
        }
        // -- 4° Cantidad de alojamientos por región
        List<AccommodationsByRegionDTO> accommodationsByRegionList = new ArrayList<>();
        respuestaQuery = adminRepository.cantidadAlojamientosPorRegion();
        for (Object[] resp: respuestaQuery){
            accommodationsByRegionList.add(new AccommodationsByRegionDTO(Integer.parseInt(resp[0].toString()),resp[1].toString(),resp[2].toString(),resp[3].toString()));
        }
        // -- 5° Cantidad de alojamientos registrados por mes
        List<AccommodatiosRegisteredByMonthDTO> accommodatiosRegisteredByMonthList = new ArrayList<>();
        respuestaQuery = adminRepository.cantidadAlijamientosRegistradosPorMes();
        for (Object[] resp: respuestaQuery){
            accommodatiosRegisteredByMonthList.add(new AccommodatiosRegisteredByMonthDTO(Integer.parseInt(resp[0].toString()),Integer.parseInt(resp[1].toString()),Integer.parseInt(resp[2].toString())));
        }
        // -- 6° Cantidad de anfitriones rechazados y aprobados por mes
        List<HostConfirmedRefusedDTO> hostConfirmedRefusedList = new ArrayList<>();
        respuestaQuery = adminRepository.cantidadAnfitrionesRechazadosConfirmados();
        for (Object[] resp: respuestaQuery){
            hostConfirmedRefusedList.add(new HostConfirmedRefusedDTO(Integer.parseInt(resp[0].toString()), Integer.parseInt(resp[1].toString()), Integer.parseInt(resp[2].toString()),Integer.parseInt(resp[3].toString())));
        }


        return new StatisticsResponse(bookingsByRegionList, registeredUserPerMonthList, accommodationsByQualificationList, accommodationsByRegionList, accommodatiosRegisteredByMonthList, hostConfirmedRefusedList);
    }

    @Override
    public List<HostBookingsPaymentsDTO> getHostsPaymentsMonthYear(int month, int year) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Object[]> respuestaQuery = adminRepository.listadoReservasAnfitriones(month, year);
        List<HostBookingsPaymentsDTO> response = new ArrayList<>();
        for (Object[] resp: respuestaQuery){
            Date date = format.parse(resp[3].toString());
            String startDate = format.format(date);
            String[] split_feature = startDate.split("-",3);
            String yyyy = split_feature[0].trim();    String mm = split_feature[1].trim();    String dd = split_feature[2].trim();
            String finalStartDate = dd+"/"+mm+"/"+yyyy;
            Integer bankNum = Integer.valueOf(resp[5].toString());
            String bank = null;
            switch (bankNum){
                case 0: bank = "SANTANDER"; break;
                case 1: bank = "BROU"; break;
                case 2: bank = "BBVA"; break;
                case 3: bank = "ITAU"; break;
                case 4: bank = "SCOTIABANK"; break;
                case 5: bank = "HSBC"; break;
                default: bank = "Banco inexistente"; break;
            }
            response.add(new HostBookingsPaymentsDTO(resp[0].toString(), Integer.parseInt(resp[1].toString()), Float.parseFloat(resp[2].toString()), finalStartDate, resp[4].toString(), bank));
        }
        return response;
    }
}
