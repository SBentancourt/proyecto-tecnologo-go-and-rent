package com.tecnologo.grupo3.goandrent.dtos;

import lombok.Data;

@Data
public class LoginDeviceDTO {
    private String email;
    private String password;
    private String deviceId;
}
