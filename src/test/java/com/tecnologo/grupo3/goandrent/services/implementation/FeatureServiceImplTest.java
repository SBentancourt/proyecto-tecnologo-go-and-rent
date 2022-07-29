package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.repositories.FeatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FeatureServiceImplTest {

    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    private FeatureServiceImpl featureService;

    private Feature feature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feature = new Feature(1, "prueba feature");
    }

    @Test
    void getFeatures() {
        when(featureRepository.findAll()).thenReturn(Arrays.asList(feature));
        assertNotNull(featureService.getFeatures());
    }

    @Test
    void findFeatureById() {
        when(featureRepository.findFeatureById(1)).thenReturn(feature);
        assertNotNull(featureService.findFeatureById(1));
    }
}