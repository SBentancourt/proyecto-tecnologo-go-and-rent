package com.tecnologo.grupo3.goandrent.dtos.responses;

import com.tecnologo.grupo3.goandrent.dtos.AccommodationInfoSearchDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Data @AllArgsConstructor
public class AccommodationsListResponse {
    private List<AccommodationInfoSearchDTO> accommodations;
    private int numeroDePagina;
    private int medidaDePagina;
    private long totalElementos;
    private int totalPaginas;
    private boolean ultimaPagina;
}
