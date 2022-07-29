package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.repositories.ServiceOfferedRepository;
import com.tecnologo.grupo3.goandrent.repositories.ServiceTypeRepository;
import com.tecnologo.grupo3.goandrent.services.ServiceOfferedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfferedServiceImpl implements ServiceOfferedService {

    @Autowired
    private ServiceOfferedRepository serviceOfferedRepository;

    @Override
    public void saveServiceOffered(ServiceType serviceType, Accommodation accommodation, boolean value) {
        serviceOfferedRepository.save(new ServiceOffered(value, serviceType, accommodation));
    }
}
