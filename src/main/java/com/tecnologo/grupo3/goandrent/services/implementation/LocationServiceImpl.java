package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.LocationDTO;
import com.tecnologo.grupo3.goandrent.entities.Location;
import com.tecnologo.grupo3.goandrent.repositories.LocationRepository;
import com.tecnologo.grupo3.goandrent.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location saveLocation(LocationDTO locationDTO) {
        return locationRepository.save(new Location(locationDTO.getCountry(), locationDTO.getProvince(),
                locationDTO.getCity(), locationDTO.getStreet(), locationDTO.getDoorNumber(), locationDTO.getCoordinates()));
    }


}
