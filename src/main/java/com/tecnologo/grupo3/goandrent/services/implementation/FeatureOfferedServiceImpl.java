package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import com.tecnologo.grupo3.goandrent.repositories.FeatureOfferedRepository;
import com.tecnologo.grupo3.goandrent.services.FeatureOfferedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureOfferedServiceImpl implements FeatureOfferedService {

    @Autowired
    private FeatureOfferedRepository featureOfferedRepository;

    @Override
    public void saveFeatureOffered(Feature feature, Accommodation accommodation, int value) {
        featureOfferedRepository.save(new FeatureOffered(value, feature, accommodation));
    }
}
