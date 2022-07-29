package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.FeatureInfoDTO;
import com.tecnologo.grupo3.goandrent.entities.Feature;

import java.util.List;

public interface FeatureService {
    Feature findFeatureById(Integer id);
    List<FeatureInfoDTO> getFeatures();
}
