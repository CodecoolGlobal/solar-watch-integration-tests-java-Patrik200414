package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.model.entity.sunset.SunsetEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SunsetRepository extends CrudRepository<SunsetEntity, Long> {
    Optional<SunsetEntity> findByCityAndDate(City city, LocalDate localDate);
}
