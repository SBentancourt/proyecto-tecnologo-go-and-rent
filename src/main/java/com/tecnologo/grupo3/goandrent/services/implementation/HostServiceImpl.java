package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.AccommodationDTO;
import com.tecnologo.grupo3.goandrent.dtos.GalleryDTO;
import com.tecnologo.grupo3.goandrent.dtos.LocationDTO;
import com.tecnologo.grupo3.goandrent.dtos.ServiceInfoDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.GuestWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.HostWithBookingsResponse;
import com.tecnologo.grupo3.goandrent.entities.*;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.*;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.services.s3Service.S3Service;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.Bank;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private ServiceOfferedService serviceOfferedService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private FeatureOfferedService featureOfferedService;

    @Autowired
    private BookingRepository bookingRepository;

    // -- Servicio para envio de emails
    @Autowired
    private EMailService eMailService;

    // -- Servicio S3
    @Autowired
    private S3Service s3Service;

    @Override
    public void saveHost(MultiValueMap<String, String> info, MultipartFile[] imagenes) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // -- obtengo los datos del map para guardar el usuario
        String alias = info.getFirst("alias");
        String email = info.getFirst("email");
        String name = info.getFirst("name");
        String lastName = info.getFirst("lastName");
        String password = info.getFirst("password");
        String phone = info.getFirst("phone");
        String birthday = info.getFirst("birthday");
        String picture = info.getFirst("picture");
        Bank bank = Bank.valueOf(info.getFirst("bank"));
        String account = info.getFirst("account");
        Date birthdayDate = format.parse(birthday);

        Host newHost = hostRepository.save(new Host(alias, email, password, name, lastName, UserStatus.ESPERANDO,
                new Date(), phone, birthdayDate, picture, bank, account));

        addAccommodation(info, imagenes, alias, true);

    }

    @Override
    public Optional<Host> getHostByEmail(String email) {
        return hostRepository.findHostByEmail(email);
    }

    @Override
    public void updateAccommodationStatus(String aliasHost, AccommodationStatus status) {
        hostRepository.updateAccommodationStatus(status, aliasHost);
    }

    @Override
    public Boolean existHostByAlias(String alias) {
        return hostRepository.existsById(alias);
    }

    @Override
    public void addAccommodation(MultiValueMap<String, String> info, MultipartFile[] imagenes, String aliasHost, Boolean newHost) {
        Host host = hostRepository.getById(aliasHost);

        // -- obtengo los datos del map para guardar la ubicación
        String loc_country = info.getFirst("loc_country");
        String loc_province = info.getFirst("loc_province");
        String loc_city = info.getFirst("loc_city");
        String loc_street = info.getFirst("loc_street");
        Integer loc_doorNumber = Integer.parseInt(info.getFirst("loc_doorNumber"));
        String loc_coordinates = info.getFirst("loc_coordinates");

        LocationDTO locationDTO = new LocationDTO(loc_country, loc_province, loc_city, loc_street, loc_doorNumber, loc_coordinates);
        Location newLocation = locationService.saveLocation(locationDTO);

        // -- obtengo las fotos, las guardo en la bd
        Set<Gallery> newGallerys = new HashSet<>();
        int cont = 1;
        for(MultipartFile i: imagenes){
            newGallerys.add(galleryService.saveGallery(new GalleryDTO("alojamiento-",i.getOriginalFilename(),cont)));
            cont++;
        }
        // -- guardo el alojamiento
        String acc_name = info.getFirst("acc_name");
        String acc_description = info.getFirst("acc_description");
        Float acc_price = Float.parseFloat(info.getFirst("acc_price"));

        AccommodationDTO accommodationDTO = new AccommodationDTO(acc_name, acc_description, acc_price, (float) 0);
        AccommodationStatus status = AccommodationStatus.ACTIVO;
        if (newHost){
            status = AccommodationStatus.BLOQUEADO;
        }
        Accommodation newAccommodation = accommodationService.saveAccommodation(accommodationDTO, newLocation, newGallerys, host, status);

        // -- actualizo las descripciones de la carpeta de la galería de fotos.
        galleryService.updateGalleryName(newAccommodation.getId(), String.valueOf(newAccommodation.getId()));

        // -- guardo los servicios del alojamiento
        List<String> servicesIncludedStr = info.get("services");

        List<Integer> servicesIncluded = new ArrayList<>();
        for (String arr: servicesIncludedStr){
            servicesIncluded.add(Integer.parseInt(arr));
        }
        List<Integer> servicesNotIncluded = serviceTypeService.servicesNotIn(servicesIncluded);
        // -- 1° agrego los servicios incluidos
        for (Integer s: servicesIncluded){
            serviceOfferedService.saveServiceOffered(serviceTypeService.findServiceTypeById(s), newAccommodation, true);
        }
        // -- 2° agrego los servicios NO incluidos
        for (Integer s2: servicesNotIncluded){
            serviceOfferedService.saveServiceOffered(serviceTypeService.findServiceTypeById(s2), newAccommodation, false);
        }

        // -- Guardo las características del alojamiento
        List<String> features = info.get("features");
        for(String f: features){
            String[] split_feature = f.split("-",2);
            Integer featureId = Integer.parseInt(split_feature[0].trim());
            Integer cFeature = Integer.parseInt(split_feature[1].trim());
            Feature feature = featureService.findFeatureById(featureId);
            featureOfferedService.saveFeatureOffered(feature, newAccommodation, cFeature);
        }

        // -- subo las fotos al servidor
        try {
            String nroAcc = String.valueOf(newAccommodation.getId());
            for(MultipartFile img: imagenes){
                s3Service.uploadFile(img.getOriginalFilename(), img.getInputStream(), "alojamiento-"+nroAcc);
            }
        } catch (Exception ex){

        }

        if (newHost){
            eMailService.sendEmailHostSignUp(adminService.getAdminsEmails(), host.getAlias(), host.getEmail(),
                    newAccommodation.getName(), newAccommodation.getId());
        }
    }

    @Override
    public List<HostWithBookingsResponse> getHostWithBookingByGuestAlias(String alias) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        List<String> hosts = hostRepository.getHostsByGuestAlias(alias, today);
        List<HostWithBookingsResponse> responses = new ArrayList<>();
        for (String h: hosts){
            List<Booking> bookings = bookingRepository.guestLastBooking(alias, h, BookingStatus.ACEPTADA, today);
            Booking booking = bookings.get(0);
            responses.add(new HostWithBookingsResponse(h, booking.getAccommodation().getHost().getName(), booking.getAccommodation().getHost().getLastName(),
                    booking.getId(), booking.getAccommodation().getName(), format.format(booking.getStartDate()),
                    format.format(booking.getEndDate())));
        }
        return responses;
    }
}
