package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Feature;

public interface FeatureOfferedService {
    void saveFeatureOffered(Feature feature, Accommodation accommodation, int value);
}
