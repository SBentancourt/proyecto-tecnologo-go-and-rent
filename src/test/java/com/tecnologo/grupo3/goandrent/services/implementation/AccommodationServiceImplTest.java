package com.tecnologo.grupo3.goandrent.services.implementation;

import com.amazonaws.util.IOUtils;
import com.tecnologo.grupo3.goandrent.dtos.AccommodationDTO;
import com.tecnologo.grupo3.goandrent.dtos.GalleryDTO;
import com.tecnologo.grupo3.goandrent.entities.*;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.entities.ids.FavoriteID;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.*;
import com.tecnologo.grupo3.goandrent.services.GalleryService;
import com.tecnologo.grupo3.goandrent.services.GeneralFeatureService;
import com.tecnologo.grupo3.goandrent.services.UserCalificationService;
import com.tecnologo.grupo3.goandrent.services.s3Service.S3Service;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
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

class AccommodationServiceImplTest {

    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private ServiceOfferedRepository serviceOfferedRepository;
    @Mock
    private FeatureOfferedRepository featureOfferedRepository;
    @Mock
    private GalleryService galleryService;
    @Mock
    private GalleryRepository galleryRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private GeneralFeatureService generalFeatureService;
    @Mock
    private UserCalificationService userCalificationService;
    @Mock
    private S3Service s3Service;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    private Accommodation accommodation;
    private AccommodationDTO accommodationDTO;
    private Set<Gallery> galleries = new HashSet<>();
    private Gallery gallery = new Gallery("name", "photo", 1);
    private Host host = new Host();
    private Location location = new Location("Uruguay", "Canelones", "Solymar", "calle", 1010, "");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        galleries.add(gallery);
        accommodation = new Accommodation("name", "description", AccommodationStatus.ACTIVO, Float.parseFloat("10"), new Date(),
                                            location, galleries, host);
        accommodationDTO = new AccommodationDTO("name", "description", Float.parseFloat("10"), Float.parseFloat("4"));
    }

    @Test
    void saveAccommodation() {
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
        assertNull(accommodationService.saveAccommodation(accommodationDTO, location, galleries, host, AccommodationStatus.ACTIVO));
    }

    @Test
    void getAccommodationObject() {
        when(accommodationRepository.getById(1)).thenReturn(accommodation);
        assertNotNull(accommodationService.getAccommodationObject(1));
    }

    @Test
    void getAccommodationsBySearch() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = format.parse("10/10/2010");
        Date endDate = format.parse("10/10/2010");
        BookingStatus activeStatus = BookingStatus.ACEPTADA;
        BookingStatus pendStatus = BookingStatus.PENDIENTE;
        AccommodationStatus accStatus = AccommodationStatus.ACTIVO;
        List<String> servicesFilter = new ArrayList<>();
        List<String> featuresFilter = new ArrayList<>();
        servicesFilter.add("1");
        featuresFilter.add("2-4");
        List<Integer> services = new ArrayList<>();
        List<Integer> features = new ArrayList<>();
        services.add(1);
        features.add(2);
        List<FeatureOffered> featureOffereds = new ArrayList<>();
        FeatureOffered featureOffered = new FeatureOffered(4, new Feature(), accommodation);
        featureOffereds.add(featureOffered);

        when(accommodationRepository.accommodationBySearchWithDates("Uruguay", "Canelones", "Solymar", startDate, endDate, activeStatus, pendStatus, accStatus))
                .thenReturn(Arrays.asList(accommodation));
        when(galleryRepository.getFirstPhotoAccommodation(0,1)).thenReturn(Optional.of(gallery));
        when(serviceOfferedRepository.getIdOfServicesOfferedByAcc(0, true)).thenReturn(services);
        when(featureOfferedRepository.findFeatureOfferedByAccommodation_Id(2)).thenReturn(featureOffereds);

        assertNotNull(accommodationService.getAccommodationsBySearch(servicesFilter, featuresFilter, "Uruguay", "Canelones", "Solymar", "10/10/2010",
                                                                        "10/10/2010", "0", "100", 0, 5));
    }

    @Test
    void getAccommodationDetails() {
        Set<FeatureOffered> featureOffereds = new HashSet<>();
        FeatureOffered featureOffered = new FeatureOffered(4, new Feature(), accommodation);
        featureOffereds.add(featureOffered);
        Set<ServiceOffered> serviceOffereds = new HashSet<>();
        ServiceOffered serviceOffered = new ServiceOffered(true, new ServiceType(1, "prueba"), accommodation);
        serviceOffereds.add(serviceOffered);
        accommodation.setServicesOffered(serviceOffereds);
        accommodation.setFeaturesOffered(featureOffereds);
        List<Booking> bookings = new ArrayList<>();
        when(accommodationRepository.findAccommodationById(0)).thenReturn(Optional.of(accommodation));
        when(accommodationRepository.getAverageReviewAccommodation(0)).thenReturn(Optional.of(Float.parseFloat("3")));
        when(bookingRepository.findBookingsByAccommodation_Id(0)).thenReturn(bookings);
        when(userCalificationService.getUserQualification("")).thenReturn(Float.parseFloat("1"));
        Optional<Favorite> favorite = Optional.of(new Favorite());
        when(favoriteRepository.findFavoriteByFavoriteID(new FavoriteID("alias", accommodation))).thenReturn(favorite);
        assertNotNull(accommodationService.getAccommodationDetails(0, "alias"));

    }

    @Test
    void deleteAccommodationById() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = null;
        try {
            today = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        when(bookingRepository.accommodationWithBookings(0,today,BookingStatus.ACEPTADA, BookingStatus.PENDIENTE)).thenReturn(false);
        when(accommodationRepository.findAccommodationById(0)).thenReturn(Optional.of(accommodation));
        when(accommodationRepository.countAccommodationsByHost(host)).thenReturn(2);
        accommodationService.deleteAccommodationById(0);

    }

    @Test
    void deleteAccommodationPhotosByHostAlias() {
        when(accommodationRepository.findAccommodationByHost_Alias("alias")).thenReturn(Optional.of(accommodation));
        accommodationService.deleteAccommodationPhotosByHostAlias("alias");
    }

    @Test
    void getAccommodationsByHost() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = null;
        try {
            today = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        when(accommodationRepository.getActiveAccommodationByHost("host", AccommodationStatus.ACTIVO)).thenReturn(Arrays.asList(accommodation));
        when(bookingRepository.reservationInProgress(0,BookingStatus.ACEPTADA, today)).thenReturn(true);
        when(galleryRepository.getFirstPhotoAccommodation(0,1)).thenReturn(Optional.of(gallery));
        accommodationService.getAccommodationsByHost("host");
    }

    @Test
    void updateAccommodationById() throws IOException {

        Set<FeatureOffered> featureOffereds = new HashSet<>();
        FeatureOffered featureOffered = new FeatureOffered(4, new Feature(2, "prueba"), accommodation);
        featureOffereds.add(featureOffered);
        Set<ServiceOffered> serviceOffereds = new HashSet<>();
        ServiceOffered serviceOffered = new ServiceOffered(true, new ServiceType(1, "prueba"), accommodation);
        serviceOffereds.add(serviceOffered);
        accommodation.setServicesOffered(serviceOffereds);
        accommodation.setFeaturesOffered(featureOffereds);

        MultipartFile[] multipartFiles = new MultipartFile[0];
        File file = new File("src/test/resources/prueba1.jpg");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image", IOUtils.toByteArray(input));
        MultiValueMap<String, String> accInfo = new LinkedMultiValueMap<String, String>();
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
        when(accommodationRepository.findAccommodationById(0)).thenReturn(Optional.of(accommodation));
        when(galleryService.saveGallery(new GalleryDTO("name", "photo", 1))).thenReturn(gallery);
        accommodationService.updateAccommodationById(0, accInfo, multipartFiles);
    }

    @Test
    void getAccommodationsByHostAlias() {
        when(accommodationRepository.findAccommodationsByHost_AliasAndStatus("alias", AccommodationStatus.BLOQUEADO)).thenReturn(Arrays.asList(accommodation));
        assertNotNull(accommodationService.getAccommodationsByHostAlias("alias"));
    }

    @Test
    void listConvertToPage1() {
    }
}