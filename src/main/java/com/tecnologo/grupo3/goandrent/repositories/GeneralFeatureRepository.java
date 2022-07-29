package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.GeneralFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralFeatureRepository extends JpaRepository<GeneralFeature, Integer> {
    GeneralFeature findGeneralFeatureById(Integer id);
}
