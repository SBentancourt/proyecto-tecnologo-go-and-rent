package com.tecnologo.grupo3.goandrent.controllers;

import com.tecnologo.grupo3.goandrent.services.AccommodationService;
import com.tecnologo.grupo3.goandrent.services.GeneralFeatureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataControllerTest {

    @Mock
    private GeneralFeatureService generalFeatureService;
    @Mock
    private AccommodationService accommodationService;

    @InjectMocks
    private DataController dataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGeneralFeatures() {
        assertNotNull(dataController.getGeneralFeatures());
    }

    @Test
    void getAccommodationInfo() {
        assertNotNull(dataController.getAccommodationInfo(1, "alias"));
    }

    @Test
    void getAccommodationsByFilter() throws ParseException {
        List<String> services = new ArrayList<>();
        List<String> features = new ArrayList<>();
        assertNotNull(dataController.getAccommodationsByFilter(services, features, "Uruguay", "Canelones", "Solymar",
                                                                "01/01/2020", "01/01/2022", "0", "999",
                                                                0,1));
    }
}