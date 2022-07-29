package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.Device;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.ids.DeviceID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, DeviceID> {
    Optional<Device> findDeviceByDeviceID(DeviceID deviceID);
    List<Device> findDevicesByDeviceID_UserAlias_Alias(String alias);
    List<Device> findDevicesByDeviceID_UserAlias(User user);
}
