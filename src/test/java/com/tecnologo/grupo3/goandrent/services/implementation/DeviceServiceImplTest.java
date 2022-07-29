package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.Device;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.ids.DeviceID;
import com.tecnologo.grupo3.goandrent.repositories.DeviceRepository;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DeviceServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDevice() {
        when(userService.getUserByAlias("alias")).thenReturn(new User());
        deviceService.saveDevice("alias", "device");
    }

    @Test
    void getDevicesTokens() {
        when(userService.getUserByAlias("alias")).thenReturn(new User());
        List<Device> devices = new ArrayList<>();
        devices.add(new Device(new DeviceID(new User(), "device")));
        when(deviceRepository.findDevicesByDeviceID_UserAlias(new User())).thenReturn(devices);
        assertNotNull(deviceService.getDevicesTokens("alias"));
    }
}