package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.AccommodationDTO;
import com.tecnologo.grupo3.goandrent.dtos.AccommodationFilterDTO;
import com.tecnologo.grupo3.goandrent.dtos.AccommodationInfoSearchDTO;
import com.tecnologo.grupo3.goandrent.dtos.HostAccommodationInfoDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.AccommodationInfoResponse;
import com.tecnologo.grupo3.goandrent.dtos.responses.AccommodationsListResponse;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.entities.Location;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccommodationService {
    Accommodation saveAccommodation(AccommodationDTO accommodationDTO, Location location, Set<Gallery> galleries, Host host, AccommodationStatus status);
    Accommodation getAccommodationObject(int id);
    AccommodationsListResponse getAccommodationsBySearch(List<String> services, List<String> features, String country, String province,
                                                            String city, String dateFrom, String dateTo, String priceFrom, String priceTo, int nroPag, int cantReg) throws ParseException;
    AccommodationInfoResponse getAccommodationDetails(int id, String alias);
    void deleteAccommodationById(int id) throws ParseException;
    void deleteAccommodationPhotosByHostAlias(String alias);
    List<HostAccommodationInfoDTO> getAccommodationsByHost(String alias);
    void updateAccommodationById(int id, MultiValueMap<String, String> accInfo, MultipartFile[] imagenes);
    List<Accommodation> getAccommodationsByHostAlias(String alias);

    Float getAverageReviewAccommodation(int id);

    List<Accommodation> getAccommodationsByHostAliasBookings(String alias);
}
