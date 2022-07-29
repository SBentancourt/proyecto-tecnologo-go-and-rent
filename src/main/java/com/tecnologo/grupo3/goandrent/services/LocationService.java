package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.LocationDTO;
import com.tecnologo.grupo3.goandrent.entities.Location;

public interface LocationService {
    Location saveLocation(LocationDTO locationDTO);
}
