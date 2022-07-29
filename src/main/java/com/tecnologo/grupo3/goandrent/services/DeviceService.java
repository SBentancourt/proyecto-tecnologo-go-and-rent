package com.tecnologo.grupo3.goandrent.services;

import java.util.List;

public interface DeviceService {
    void saveDevice(String alias, String deviceId);
    List<String> getDevicesTokens(String alias);
}
