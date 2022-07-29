package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import com.tecnologo.grupo3.goandrent.entities.ids.FeatureID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeatureOfferedRepository extends JpaRepository<FeatureOffered, FeatureID> {
    List<FeatureOffered> findFeatureOfferedByAccommodation_Id(int id);
}
