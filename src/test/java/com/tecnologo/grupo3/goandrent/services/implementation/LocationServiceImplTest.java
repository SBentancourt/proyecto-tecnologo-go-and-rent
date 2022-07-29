package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.LocationDTO;
import com.tecnologo.grupo3.goandrent.entities.Location;
import com.tecnologo.grupo3.goandrent.repositories.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        location = new Location("Uruguay", "Canelones", "Solymar", "Calle 1", 1010, "");
        locationDTO = new LocationDTO("Uruguay", "Canelones", "Solymar", "Calle 1", 1010, "");
    }

    @Test
    void saveLocation() {
        when(locationRepository.save(location)).thenReturn(location);
        assertNotNull(locationService.saveLocation(locationDTO));
    }
}