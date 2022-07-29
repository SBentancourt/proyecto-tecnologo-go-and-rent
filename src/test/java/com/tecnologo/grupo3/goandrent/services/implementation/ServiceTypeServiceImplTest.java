package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.repositories.ServiceTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ServiceTypeServiceImplTest {

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @InjectMocks
    private ServiceTypeServiceImpl serviceTypeService;

    private Optional<ServiceType> serviceType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceType = Optional.of(new ServiceType(1, "prueba service"));
    }

    @Test
    void findServiceTypeById() {
        when(serviceTypeRepository.findById(1)).thenReturn(serviceType);
        assertNotNull(serviceTypeService.findServiceTypeById(1));
    }

    @Test
    void getServices() {
        when(serviceTypeRepository.findAll()).thenReturn(Arrays.asList(serviceType.get()));
        assertNotNull(serviceTypeService.getServices());
    }

    @Test
    void servicesNotIn() {
        List<Integer> ids = new ArrayList<>(); ids.add(2);
        when(serviceTypeRepository.findServiceTypeByIdNotIn(ids)).thenReturn(Arrays.asList(serviceType.get()));
        assertNotNull(serviceTypeService.servicesNotIn(ids));
    }
}