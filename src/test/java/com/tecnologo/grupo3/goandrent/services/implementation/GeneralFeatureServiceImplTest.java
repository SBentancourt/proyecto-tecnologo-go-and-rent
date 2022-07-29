package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.FeatureInfoDTO;
import com.tecnologo.grupo3.goandrent.dtos.ServiceInfoDTO;
import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.entities.GeneralFeature;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.repositories.GeneralFeatureRepository;
import com.tecnologo.grupo3.goandrent.services.FeatureService;
import com.tecnologo.grupo3.goandrent.services.GeneralFeatureService;
import com.tecnologo.grupo3.goandrent.services.ServiceTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GeneralFeatureServiceImplTest {

    @Mock
    private FeatureService featureService;
    @Mock
    private ServiceTypeService serviceTypeService;
    @Mock
    private GeneralFeatureRepository generalFeatureRepository;

    @InjectMocks
    private GeneralFeatureServiceImpl generalFeatureService;

    private GeneralFeature generalFeature;
    private List<ServiceInfoDTO> listServices = new ArrayList<>();
    private List<FeatureInfoDTO> listFeatures = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        generalFeature = new GeneralFeature(1, "Prueba General");
        listServices.add(new ServiceInfoDTO(1, "Servicio"));
        listFeatures.add(new FeatureInfoDTO(2, "Feature"));
    }

    @Test
    void getGeneralFeaturesList() {
        when(serviceTypeService.getServices()).thenReturn(listServices);
        when(featureService.getFeatures()).thenReturn(listFeatures);
        assertNotNull(generalFeatureService.getGeneralFeaturesList());
    }

    @Test
    void getNameById() {
        when(generalFeatureRepository.findGeneralFeatureById(1)).thenReturn(generalFeature);
        assertNotNull(generalFeatureService.getNameById(1));
    }
}