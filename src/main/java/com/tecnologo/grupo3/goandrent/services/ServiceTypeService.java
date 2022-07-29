package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.ServiceInfoDTO;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;

import java.util.List;

public interface ServiceTypeService {
    ServiceType findServiceTypeById(Integer id);
    List<ServiceInfoDTO> getServices();
    List<Integer> servicesNotIn(List<Integer> ids);
}
