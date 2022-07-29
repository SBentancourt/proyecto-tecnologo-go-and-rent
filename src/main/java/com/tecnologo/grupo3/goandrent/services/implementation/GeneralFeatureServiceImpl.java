package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.GeneralFeaturesDTO;
import com.tecnologo.grupo3.goandrent.repositories.GeneralFeatureRepository;
import com.tecnologo.grupo3.goandrent.services.FeatureService;
import com.tecnologo.grupo3.goandrent.services.GeneralFeatureService;
import com.tecnologo.grupo3.goandrent.services.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralFeatureServiceImpl implements GeneralFeatureService {

    @Autowired
    private FeatureService featureService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private GeneralFeatureRepository generalFeatureRepository;

    @Override
    public GeneralFeaturesDTO getGeneralFeaturesList() {
        return new GeneralFeaturesDTO(serviceTypeService.getServices(),featureService.getFeatures());
    }

    @Override
    public String getNameById(int id) {
        return generalFeatureRepository.findGeneralFeatureById(id).getName();
    }
}
