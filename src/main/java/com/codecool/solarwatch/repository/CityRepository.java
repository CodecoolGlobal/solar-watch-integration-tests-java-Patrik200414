package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entities.city.City;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Long> {
    Optional<City> findByName(String name);
}
