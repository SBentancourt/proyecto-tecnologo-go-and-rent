package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    Feature findFeatureById(Integer id);
}
