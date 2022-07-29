package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class GeneralFeaturesDTO {
    private List<ServiceInfoDTO> servicios;
    private List<FeatureInfoDTO> caracteristicas;
}
