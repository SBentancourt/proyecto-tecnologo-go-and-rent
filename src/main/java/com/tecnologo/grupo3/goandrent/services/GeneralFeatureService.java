package com.tecnologo.grupo3.goandrent.services;


import com.tecnologo.grupo3.goandrent.dtos.GeneralFeaturesDTO;

public interface GeneralFeatureService {
    GeneralFeaturesDTO getGeneralFeaturesList();
    String getNameById(int id);
}
