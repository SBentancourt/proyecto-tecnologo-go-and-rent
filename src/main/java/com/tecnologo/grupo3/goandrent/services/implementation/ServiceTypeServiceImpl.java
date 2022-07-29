package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.ServiceInfoDTO;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.repositories.ServiceTypeRepository;
import com.tecnologo.grupo3.goandrent.services.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public ServiceType findServiceTypeById(Integer id) {
        Optional<ServiceType> serviceType = serviceTypeRepository.findById(id);
        return serviceType.get();
    }

    @Override
    public List<ServiceInfoDTO> getServices() {
        List<ServiceType> servicesList = serviceTypeRepository.findAll();
        List<ServiceInfoDTO> services = new ArrayList<>();
        for (ServiceType s: servicesList){
            services.add(new ServiceInfoDTO(s.getId(), s.getName()));
        }
        return services;
    }

    @Override
    public List<Integer> servicesNotIn(List<Integer> ids) {
        List<ServiceType> services = serviceTypeRepository.findServiceTypeByIdNotIn(ids);
        List<Integer> servicesIds = new ArrayList<>();
        for (ServiceType s: services){
            servicesIds.add(s.getId());
        }
        return servicesIds;
    }
}
