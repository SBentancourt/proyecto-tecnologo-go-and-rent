package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
    @Override
    Optional<ServiceType> findById(Integer integer);

    List<ServiceType> findServiceTypeByIdNotIn(Collection<Integer> id);
}
