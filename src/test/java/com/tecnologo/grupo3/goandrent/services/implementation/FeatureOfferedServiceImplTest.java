package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.repositories.FeatureOfferedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class FeatureOfferedServiceImplTest {

    @Mock
    private FeatureOfferedRepository featureOfferedRepository;

    @InjectMocks
    private FeatureOfferedServiceImpl featureOfferedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveFeatureOffered() {
        featureOfferedService.saveFeatureOffered(new Feature(), new Accommodation(), 1);
    }
}