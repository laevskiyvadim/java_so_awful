package org.example.avtodiller.repositories;

import org.example.avtodiller.models.CityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<CityModel, Long> {
    Optional<CityModel> findByName(String name);
}
