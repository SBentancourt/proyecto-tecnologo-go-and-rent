package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.*;
import com.tecnologo.grupo3.goandrent.dtos.responses.AccommodationInfoResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.AccommodationsListResponse;
import com.tecnologo.grupo3.goandrent.entities.*;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.entities.ids.FavoriteID;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.exceptions.AccommodationException;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.*;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.services.s3Service.S3Service;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccommodationServiceImpl implements AccommodationService {

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private ServiceOfferedRepository serviceOfferedRepository;

    @Autowired
    private FeatureOfferedRepository featureOfferedRepository;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private GeneralFeatureService generalFeatureService;

    @Autowired
    private UserCalificationService userCalificationService;

    @Autowired
    private S3Service s3Service;

    @Override
    public Accommodation saveAccommodation(AccommodationDTO accommodationDTO, Location location, Set<Gallery> galleries, Host host, AccommodationStatus status) {
        return accommodationRepository.save(new Accommodation(accommodationDTO.getName(), accommodationDTO.getDescription(),
                status, accommodationDTO.getPrice(), new Date(), location, galleries, host));
    }

    @Override
    public Accommodation getAccommodationObject(int id) {
        return accommodationRepository.getById(id);
    }

    @Override
    public AccommodationsListResponse getAccommodationsBySearch(List<String> servicesFilter, List<String> featuresFilter, String country, String province,
                                                                     String city, String dateFrom, String dateTo, String priceFrom, String priceTo, int nroPag, int cantReg) throws ParseException {
        List<Integer> posiciones = new ArrayList<>();

        List<Accommodation> accommodations = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // -- aca obtengo los alojamientos de determinada ubicación.
        // -- si se ingresan fechas, se devuelven únicamente aquellos que NO tienen reservas pendientes o aceptadas en el rango de fechas indicados.
        AccommodationStatus statusAcc = AccommodationStatus.ACTIVO;
        if (dateFrom != null && !dateFrom.isEmpty() && dateTo != null && !dateTo.isEmpty()) {
            Date startDate = format.parse(dateFrom);
            Date endDate = format.parse(dateTo);
            BookingStatus activeStatus = BookingStatus.ACEPTADA;
            BookingStatus pendStatus = BookingStatus.PENDIENTE;
            accommodations = accommodationRepository.accommodationBySearchWithDates(country, province, city, startDate, endDate, activeStatus, pendStatus, statusAcc);
        } else {
            accommodations = accommodationRepository.accommodationBySearchNoDates(country, province, city, statusAcc);
        }
        // -- rango de precio
        if (priceFrom != null && priceTo != null) {
            for (Accommodation a : accommodations) {
                // -- si el precio del alojamiento no está en el rango ingresado, quito el alojamiento de la lista.
                if (!(a.getPrice() >= Float.parseFloat(priceFrom) && a.getPrice() <= Float.parseFloat(priceTo))) {
                    //accommodations.remove(a);
                    posiciones.add(accommodations.indexOf(a));
                }
            }
        }
        posiciones.sort((o1, o2) -> o2.compareTo(o1));
        for (Integer i : posiciones) {
            accommodations.remove(i.intValue());
        }
        posiciones.clear();

        // -- servicios
        if (servicesFilter != null) {
            for (Accommodation a : accommodations) {
                int totalServices = servicesFilter.size();
                // -- obtengo todos los servicios que tiene el alojamiento
                List<Integer> services = serviceOfferedRepository.getIdOfServicesOfferedByAcc(a.getId(), true);
                // -- si los servicios pasados en el filtro son menos que los que tiene el alojamiento, ya ni busco, sé que no cumple y lo saco de la lista
                if (totalServices <= services.size()) {

                    int cont = 0;
                    // -- recorro los servicios del filtro y verifico si están en la lista de servicios del alojamiento
                    for (String s : servicesFilter) {
                        if (services.contains(Integer.parseInt(s))) {
                            cont++;
                        }
                    }
                    // -- si el alojamiento no tiene todos los servicios del filtro no lo considero en el resultado
                    if (cont < totalServices) {
                        posiciones.add(accommodations.indexOf(a));
                    }
                } else {
                    posiciones.add(accommodations.indexOf(a));
                }
            }
        }
        posiciones.sort((o1, o2) -> o2.compareTo(o1));
        for (Integer i : posiciones) {
            accommodations.remove(i.intValue());
        }
        posiciones.clear();

        // -- caracteristicas
        if (featuresFilter != null) {
            for (Accommodation a : accommodations) {
                List<FeatureOffered> features = featureOfferedRepository.findFeatureOfferedByAccommodation_Id(a.getId());
                Boolean invalidFeatures = false;
                for (String f : featuresFilter) {
                    String[] split_feature = f.split("-", 2);
                    Integer featureId = Integer.parseInt(split_feature[0].trim());
                    Integer cFeature = Integer.parseInt(split_feature[1].trim());
                    for (FeatureOffered fo : features) {
                        if ((fo.getFeatureID().getFeatureId() == featureId) && (fo.getValue() != cFeature)) {
                            invalidFeatures = true;
                            break;
                        }
                    }
                    if (invalidFeatures) {
                        break;
                    }
                }
                if (invalidFeatures) {
                    //accommodations.remove(a);
                    posiciones.add(accommodations.indexOf(a));
                }
            }
        }

        posiciones.sort((o1, o2) -> o2.compareTo(o1));
        for (Integer i : posiciones) {
            accommodations.remove(i.intValue());
        }
        posiciones.clear();

        // -- obtener calificación de los alojamientos y armado de objeto de respuesta
        List<AccommodationInfoSearchDTO> accommodationInfoSearchDTOList = new ArrayList<>();
        DecimalFormatSymbols simbols = new DecimalFormatSymbols();
        simbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("####.##", simbols);
        for (Accommodation a : accommodations) {
            int idAccommodation = a.getId();
            Optional<Float> qualification = accommodationRepository.getAverageReviewAccommodation(idAccommodation);
            int orderPhoto = 1;
            Optional<Gallery> gallery = galleryRepository.getFirstPhotoAccommodation(idAccommodation, orderPhoto);
            String photo = gallery.get().getName()+"/"+gallery.get().getPhoto();
            accommodationInfoSearchDTOList.add(new AccommodationInfoSearchDTO(a.getId(), a.getName(), a.getDescription(),
                    a.getPrice(), photo, Float.parseFloat(decimalFormat.format(qualification.orElseGet(() -> Float.valueOf(0))))));
        }

        Pageable pageable = PageRequest.of(nroPag, cantReg);
        Page<AccommodationInfoSearchDTO> listResultPage = listConvertToPage1(accommodationInfoSearchDTOList, pageable);

        return new AccommodationsListResponse(listResultPage.getContent(), listResultPage.getNumber(), listResultPage.getSize(),
                listResultPage.getTotalElements(), listResultPage.getTotalPages(), listResultPage.isLast());
    }

    @Override
    public AccommodationInfoResponse getAccommodationDetails(int id, String alias) {
        DecimalFormatSymbols simbols = new DecimalFormatSymbols();
        simbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("####.##", simbols);

        Accommodation accommodation = accommodationRepository.findAccommodationById(id)
                .orElseThrow(() -> new AccommodationException(HttpStatus.NOT_FOUND, "Alojamiento no encontrado con id: " + id));


        // -- Datos del alojamiento
        Optional<Float> qualification = accommodationRepository.getAverageReviewAccommodation(id);
        AccommodationDTO accommodationDTO = new AccommodationDTO(accommodation.getName(), accommodation.getDescription(), accommodation.getPrice(),
                                            Float.parseFloat(decimalFormat.format(qualification.orElseGet(() -> Float.valueOf(0)))));
        // -- Datos de la ubicación
        Location location = accommodation.getLocation();
        LocationDTO locationDTO = new LocationDTO(location.getCountry(), location.getProvince(), location.getCity(), location.getStreet(), location.getDoorNumber(), location.getCoordinates());
        // -- Datos de las fotos
        Set<Gallery> galleries = accommodation.getGallery();
        List<PhotoDTO> photosDTO = new ArrayList<>();
        for(Gallery g: galleries){
            String photo = g.getName()+"/"+g.getPhoto();
            PhotoDTO photoDTO = new PhotoDTO(photo, g.getOrderPhoto());
            photosDTO.add(photoDTO);
        }

        // -- Datos de los servicios
        Set<ServiceOffered> serviceOffereds = accommodation.getServicesOffered();
        List<ServiceValueAccDTO> serviceValueAccDTOS = new ArrayList<>();

        for(ServiceOffered s: serviceOffereds){

            ServiceValueAccDTO serviceValueAccDTO = new ServiceValueAccDTO(s.getServiceID().getServiceId(), s.getServiceType().getName(),s.getValue());
            serviceValueAccDTOS.add(serviceValueAccDTO);
        }
        // -- Datos de las caracteristicas
        Set<FeatureOffered> featureOffereds = accommodation.getFeaturesOffered();
        List<FeatureValueAccDTO> featureValueAccDTOS = new ArrayList<>();
        for (FeatureOffered f: featureOffereds){
            FeatureValueAccDTO featureValueAccDTO = new FeatureValueAccDTO(f.getFeatureID().getFeatureId(), f.getFeature().getName() ,f.getValue());
            featureValueAccDTOS.add(featureValueAccDTO);
        }
        // -- Datos de las reviews
        List<Booking> bookings = bookingRepository.findBookingsByAccommodation_Id(id);
        List<ReviewInfoDTO> reviewInfoDTOS = new ArrayList<>();
        ReviewInfoDTO reviewInfoDTO = null;
        for (Booking b: bookings){
            if (b.getGuest() != null && (b.getReview() != null)) {
                reviewInfoDTO = new ReviewInfoDTO(b.getReview().getQualification(), b.getReview().getDescription(),
                        b.getGuest().getName(), b.getGuest().getPicture());
                reviewInfoDTOS.add(reviewInfoDTO);
            }
        }
        String hostAlias = accommodation.getHost().getAlias();
        Float hostQualification = Float.parseFloat(decimalFormat.format(userCalificationService.getUserQualification(hostAlias).floatValue()));
        HostInfoDTO hostInfoDTO = new HostInfoDTO(accommodation.getHost().getAlias(), accommodation.getHost().getName(), accommodation.getHost().getPicture() ,hostQualification);

        // -- Si el alias existe busco si el alojamiento es favorito del usuario
        Boolean isFavorite = false;
        if (alias != null){
            FavoriteID favoriteID = new FavoriteID(alias, accommodation);
            isFavorite = favoriteRepository.findFavoriteByFavoriteID(favoriteID).isPresent();
        }

        return new AccommodationInfoResponse(hostInfoDTO, accommodationDTO, locationDTO, photosDTO, serviceValueAccDTOS, featureValueAccDTOS, reviewInfoDTOS, isFavorite);
    }

    @Override
    public void deleteAccommodationById(int id) {
        // -- 1° Se valida si hay reservas pendientes
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = null;
        try {
            today = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Boolean accWithBookings = bookingRepository.accommodationWithBookings(id, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE);
        if (accWithBookings){
            throw new AccommodationException(HttpStatus.BAD_REQUEST, "El alojamiento tiene reservas activas o pendientes");
        }
        try {
            Accommodation acc = accommodationRepository.findAccommodationById(id)
                    .orElseThrow(() -> new AccommodationException(HttpStatus.NOT_FOUND, "Alojamiento no encontrado con id: " + id));

            Integer cantAcc = accommodationRepository.countAccommodationsByHost(acc.getHost());
            if (cantAcc == 1) {
                throw new AccommodationException(HttpStatus.BAD_REQUEST, "No se puede eliminar el único alojamiento que tiene. Contáctese con un administrador.");
            }

            Set<Gallery> galleries = acc.getGallery();
            List<String> images = new ArrayList<>();
            for (Gallery g: galleries){
                images.add(g.getName()+"/"+g.getPhoto());
            }

            acc.setStatus(AccommodationStatus.ELIMINADO);
            accommodationRepository.save(acc);

            s3Service.deleteFolder(images);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAccommodationPhotosByHostAlias(String alias) {
        Accommodation accommodation = accommodationRepository.findAccommodationByHost_Alias(alias)
                .orElseThrow(() -> new AccommodationException(HttpStatus.NOT_FOUND, "No se encontró ningún alojamiento para el anfitrión con alias: " + alias));

        Set<Gallery> galleries = accommodation.getGallery();
        List<String> images = new ArrayList<>();
        for (Gallery g: galleries){
            images.add(g.getName()+"/"+g.getPhoto());
        }
        String name = "alojamiento-"+accommodation.getId();
        //galleryRepository.deleteAll(galleries); // -- borro la tabla de galerías
        galleryRepository.deleteGalleriesByName(name);

        s3Service.deleteFolder(images);
    }

    @Override
    public List<HostAccommodationInfoDTO> getAccommodationsByHost(String alias) {
        AccommodationStatus activeStatus = AccommodationStatus.ACTIVO;
        List<Accommodation> hostAccommodations = accommodationRepository.getActiveAccommodationByHost(alias, activeStatus);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = null;
        try {
            today = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<HostAccommodationInfoDTO> resultList = new ArrayList<>();
        BookingStatus activeBookingStatus = BookingStatus.ACEPTADA;
        for (Accommodation a: hostAccommodations){
            int accommodationId = a.getId();
            Boolean reservationInProgress = bookingRepository.reservationInProgress(accommodationId, activeBookingStatus, today);
            Gallery firstPhoto = galleryRepository.getFirstPhotoAccommodation(a.getId(), 1)
                    .orElseThrow(() -> new AccommodationException(HttpStatus.NOT_FOUND, "El alojamiento con nombre " + a.getName() + " no tiene foto principal."));
            String photo = firstPhoto.getName()+"/"+firstPhoto.getPhoto();
            resultList.add(new HostAccommodationInfoDTO(a.getId(), a.getName(), a.getDescription(), reservationInProgress, a.getStatus().toString(), photo));
        }
        return resultList;
    }

    @Override
    public void updateAccommodationById(int id, MultiValueMap<String, String> accInfo, MultipartFile[] imagenes) {
        Accommodation accommodation = accommodationRepository.findAccommodationById(id)
                .orElseThrow(() -> new AccommodationException(HttpStatus.NOT_FOUND, "Alojamiento no encontrado con id: " + id));

        // -- actualizo datos del alojamiento
        accommodation.setName(accInfo.getFirst("acc_name"));
        accommodation.setDescription(accInfo.getFirst("acc_description"));
        accommodation.setPrice(Float.valueOf(accInfo.getFirst("acc_price")));

        // -- actualizo ubicación
        Location location = accommodation.getLocation();
        // -- obtengo los datos del map para actualizar la ubicación
        location.setCountry(accInfo.getFirst("loc_country"));
        location.setProvince(accInfo.getFirst("loc_province"));
        location.setCity(accInfo.getFirst("loc_city"));
        location.setStreet(accInfo.getFirst("loc_street"));
        location.setDoorNumber(Integer.parseInt(accInfo.getFirst("loc_doorNumber")));
        location.setCoordinates(accInfo.getFirst("loc_coordinates"));

        // -- actualizo los servicios
        List<String> servicesIncludedStr = accInfo.get("services");
        Set<ServiceOffered> serviceOffereds = accommodation.getServicesOffered();
        for (ServiceOffered s: serviceOffereds){
            // -- primero lo seteo en false
            s.setValue(false);
            // -- si el id del servicio está en la lista que recibo, actualizo el servicio a true.
            for (String str: servicesIncludedStr){
                if (Integer.parseInt(str) == s.getServiceID().getServiceId()){
                    s.setValue(true);
                }
            }
        }

        // -- actualizo las características del alojamiento
        List<String> features = accInfo.get("features");
        Set<FeatureOffered> featureOffereds = accommodation.getFeaturesOffered();
        for(String f: features){
            String[] split_feature = f.split("-",2);
            Integer featureId = Integer.parseInt(split_feature[0].trim());
            Integer cFeature = Integer.parseInt(split_feature[1].trim());
            for (FeatureOffered fo: featureOffereds){
                if (featureId == fo.getFeatureID().getFeatureId()){
                    fo.setValue(cFeature);
                }
            }
        }

        // -- actualizo las fotos
        if (imagenes != null) {
            Set<Gallery> galleries = accommodation.getGallery();
            List<String> images = new ArrayList<>();
            for (Gallery g : galleries) {
                images.add(g.getName() + "/" + g.getPhoto());
            }
            String name = "alojamiento-" + id;
            galleryRepository.deleteGalleriesByName(name);
            //galleryRepository.deleteAll(galleries); // -- borro la tabla de galerías
            // -- primero borro todas las fotos del servidor
            s3Service.deleteFolder(images);
            // -- despues actualizo el nombre de las fotos en la bd
            Set<Gallery> newGallerys = new HashSet<>();
            int cont = 1;
            for (MultipartFile i : imagenes) {
                newGallerys.add(galleryService.saveGallery(new GalleryDTO("alojamiento-" + accommodation.getId(), i.getOriginalFilename(), cont)));
                cont++;
            }

            accommodation.setGallery(newGallerys);
        }

        accommodation.setLocation(location);
        accommodation.setServicesOffered(serviceOffereds);
        accommodation.setFeaturesOffered(featureOffereds);

        accommodationRepository.save(accommodation);

        // -- subo las fotos al servidor
        if (imagenes != null) {
            try {
                String nroAcc = String.valueOf(accommodation.getId());
                for (MultipartFile img : imagenes) {
                    s3Service.uploadFile(img.getOriginalFilename(), img.getInputStream(), "alojamiento-" + nroAcc);
                }
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public List<Accommodation> getAccommodationsByHostAlias(String alias) {
        return accommodationRepository.findAccommodationsByHost_AliasAndStatus(alias, AccommodationStatus.BLOQUEADO);
    }

    @Override
    public Float getAverageReviewAccommodation(int id) {
        return accommodationRepository.getAverageReviewAccommodation(id).orElse((float) 0);
    }

    @Override
    public List<Accommodation> getAccommodationsByHostAliasBookings(String alias) {
        return accommodationRepository.findAccommodationsByHost_Alias(alias);
    }


    public static <T> Page<T> listConvertToPage1(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());

    }
}