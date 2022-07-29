package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.FeatureInfoDTO;
import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.repositories.FeatureRepository;
import com.tecnologo.grupo3.goandrent.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public Feature findFeatureById(Integer id) {
        return featureRepository.findFeatureById(id);
    }

    @Override
    public List<FeatureInfoDTO> getFeatures() {
        List<Feature> featuresList = featureRepository.findAll();
        List<FeatureInfoDTO> features = new ArrayList<>();
        for (Feature f: featuresList){
            features.add(new FeatureInfoDTO(f.getId(), f.getName()));
        }
        return features;
    }
}
