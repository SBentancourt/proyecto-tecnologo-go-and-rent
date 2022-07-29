package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Device;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.ids.DeviceID;
import com.tecnologo.grupo3.goandrent.repositories.DeviceRepository;
import com.tecnologo.grupo3.goandrent.services.DeviceService;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userService;

    @Override
    public void saveDevice(String alias, String deviceId) {
        User user = userService.getUserByAlias(alias);
        DeviceID deviceID = new DeviceID(user, deviceId);
        deviceRepository.save(new Device(deviceID));
    }

    @Override
    public List<String> getDevicesTokens(String alias) {
        User user = userService.getUserByAlias(alias);
        List<Device> devices = deviceRepository.findDevicesByDeviceID_UserAlias(user);
        List<String> result = new ArrayList<>();
        for (Device d: devices){
            result.add(d.getDeviceID().getDeviceId());
        }
        return result;
    }
}
