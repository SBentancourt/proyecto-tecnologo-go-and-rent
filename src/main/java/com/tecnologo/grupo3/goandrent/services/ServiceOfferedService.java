package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;

public interface ServiceOfferedService {
    void saveServiceOffered(ServiceType serviceType, Accommodation accommodation, boolean value);
}
