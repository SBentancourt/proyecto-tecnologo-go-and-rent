package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.repositories.ServiceOfferedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ServiceOfferedServiceImplTest {

    @Mock
    private ServiceOfferedRepository serviceOfferedRepository;

    @InjectMocks
    private ServiceOfferedServiceImpl serviceOfferedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveServiceOffered() {
        when(serviceOfferedRepository.save(new ServiceOffered())).thenReturn(new ServiceOffered());
        serviceOfferedService.saveServiceOffered(new ServiceType(), new Accommodation(), true);
    }
}