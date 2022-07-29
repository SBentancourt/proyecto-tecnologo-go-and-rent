package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.entities.ids.ServiceID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOfferedRepository extends JpaRepository<ServiceOffered, ServiceID> {
    List<ServiceOffered> findServiceOfferedByAccommodation_IdAndValue(int id, boolean value);


    @Query(value = "select service_id from service_offered where accommodation_id = :accommodationId and value = :value", nativeQuery = true)
    List<Integer> getIdOfServicesOfferedByAcc(@Param("accommodationId") Integer accommodationId, @Param("value") Boolean value);

}
