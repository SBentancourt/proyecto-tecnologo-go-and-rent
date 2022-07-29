package com.tecnologo.grupo3.goandrent.services.implementation;

import com.amazonaws.util.IOUtils;
import com.tecnologo.grupo3.goandrent.dtos.AccommodationDTO;
import com.tecnologo.grupo3.goandrent.dtos.GalleryDTO;
import com.tecnologo.grupo3.goandrent.dtos.LocationDTO;
import com.tecnologo.grupo3.goandrent.entities.*;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.BookingRepository;
import com.tecnologo.grupo3.goandrent.repositories.HostRepository;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.services.s3Service.S3Service;
import com.tecnologo.grupo3.goandrent.utils.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HostServiceImplTest {

    @Mock
    private AdminService adminService;
    @Mock
    private HostRepository hostRepository;
    @Mock
    private AccommodationService accommodationService;
    @Mock
    private LocationService locationService;
    @Mock
    private GalleryService galleryService;
    @Mock
    private ServiceOfferedService serviceOfferedService;
    @Mock
    private ServiceTypeService serviceTypeService;
    @Mock
    private FeatureService featureService;
    @Mock
    private FeatureOfferedService featureOfferedService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private EMailService eMailService;
    @Mock
    private S3Service s3Service;

    @InjectMocks
    private HostServiceImpl hostService;

    private Host host;
    private MultiValueMap<String, String> accInfo = new LinkedMultiValueMap<String, String>();
    private Location location;
    private Gallery gallery;
    private Accommodation accommodation;
    private Set<Gallery> galleries = new HashSet<>();
    private FeatureOffered featureOffered;
    private Set<FeatureOffered> featureOffereds = new HashSet<>();
    private ServiceOffered serviceOffered;
    private Set<ServiceOffered> serviceOffereds = new HashSet<>();
    private List<Integer> servicesNotIncluded = new ArrayList<>();
    private Feature feature;
    private ServiceType serviceType;
    private ServiceType serviceType2;

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.openMocks(this);


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        // -- ** Seteo MultiValueMap ** -- //
        // -- Host Info
        accInfo.set("alias", "host");
        accInfo.set("email", "host@test.com");
        accInfo.set("name", "name");
        accInfo.set("lastName", "lastname");
        accInfo.set("password", "password");
        accInfo.set("phone", "0123456");
        accInfo.set("birthday", fecha);
        accInfo.set("picture", "");
        accInfo.set("bank", "ITAU");
        accInfo.set("account", "000000");
        // -- Accommodation Info
        accInfo.set("acc_name", "prueba");
        accInfo.set("acc_description", "prueba");
        accInfo.set("acc_price", "10");
        accInfo.set("loc_country", "Uruguay");
        accInfo.set("loc_province", "Canelones");
        accInfo.set("loc_city", "Solymar");
        accInfo.set("loc_street", "calle");
        accInfo.set("loc_doorNumber", "1000");
        accInfo.set("services", "1");
        accInfo.set("features", "2-3");

        host = new Host("host", "host@test.com", "password", "name", "lastname", UserStatus.ESPERANDO, new Date(), "0123456",
                format.parse(fecha), "", Bank.ITAU, "000000");

        location = new Location("Uruguay", "Canelones", "Solymar", "calle", 1000, "");
        gallery = new Gallery("name", "photo", 1);
        galleries.add(gallery);
        accommodation = new Accommodation("prueba", "prueba", AccommodationStatus.BLOQUEADO, Float.parseFloat("10"), new Date(), location,
                                            galleries, host);

        feature = new Feature(2, "prueba");
        serviceType = new ServiceType(1, "prueba");
        serviceType2 = new ServiceType(3, "prueba3");
        /*featureOffered = new FeatureOffered(3, feature, accommodation);
        featureOffereds.add(featureOffered);
        serviceOffered = new ServiceOffered(true, serviceType, accommodation);
        serviceOffereds.add(serviceOffered);*/

        servicesNotIncluded.add(3);

    }

    @Test
    void saveHost() throws IOException, ParseException {

        LocationDTO locationDTO = new LocationDTO("Uruguay", "Canelones", "Solymar", "calle", 1000, null);
        GalleryDTO galleryDTO = new GalleryDTO("alojamiento-", "prueba1.jpg", 1);
        AccommodationDTO accommodationDTO = new AccommodationDTO("prueba", "prueba", Float.parseFloat("10"), (float) 0);
        AccommodationStatus status = AccommodationStatus.BLOQUEADO;
        MultipartFile[] multipartFiles = new MultipartFile[1];
        File file = new File("src/test/resources/prueba1.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image", IOUtils.toByteArray(input));
        multipartFiles[0] = multipartFile;

        when(hostRepository.save(host)).thenReturn(host);
        when(hostRepository.getById("host")).thenReturn(host);
        when(locationService.saveLocation(locationDTO)).thenReturn(location);
        when(galleryService.saveGallery(galleryDTO)).thenReturn(gallery);
        when(accommodationService.saveAccommodation(accommodationDTO,location,galleries,host,status)).thenReturn(accommodation);
        when(serviceTypeService.servicesNotIn(Arrays.asList(1))).thenReturn(servicesNotIncluded);
        when(serviceTypeService.findServiceTypeById(1)).thenReturn(serviceType);
        when(serviceTypeService.findServiceTypeById(3)).thenReturn(serviceType2);
        when(featureService.findFeatureById(2)).thenReturn(feature);

        hostService.saveHost(accInfo, multipartFiles);

    }

    @Test
    void getHostByEmail() {
        when(hostRepository.findHostByEmail("host@test.com")).thenReturn(Optional.of(host));
        assertNotNull(hostService.getHostByEmail("host@test.com"));
    }

    @Test
    void updateAccommodationStatus() {
        hostService.updateAccommodationStatus("host", AccommodationStatus.ACTIVO);
    }

    @Test
    void existHostByAlias() {
        when(hostRepository.existsById("host")).thenReturn(true);
        assertTrue(hostService.existHostByAlias("host"));
    }

    @Test
    void getHostWithBookingByGuestAlias() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        Guest guest = new Guest("guest", "guest@test.com", "password", "name", "lastname", UserStatus.ESPERANDO, new Date(), "0123456",
                format.parse(fecha), "");
        Booking booking = new Booking(new Date(), new Date(), BookingStatus.ACEPTADA, PaymentStatus.COMPLETADO, Float.parseFloat("10"), accommodation, guest);
        when(hostRepository.getHostsByGuestAlias("guest", today)).thenReturn(Arrays.asList("host"));
        when(bookingRepository.guestLastBooking("guest", "host", BookingStatus.ACEPTADA, today)).thenReturn(Arrays.asList(booking));
        assertNotNull(hostService.getHostWithBookingByGuestAlias("guest"));
    }
}