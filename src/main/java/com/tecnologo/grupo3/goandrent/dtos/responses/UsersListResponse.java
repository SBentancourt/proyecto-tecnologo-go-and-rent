package com.tecnologo.grupo3.goandrent.dtos.responses;

import com.tecnologo.grupo3.goandrent.dtos.UserInformationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class UsersListResponse {
    private List<UserInformationDTO> usuarios;
}
