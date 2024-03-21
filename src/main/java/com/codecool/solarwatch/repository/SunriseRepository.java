package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.model.entity.sunrise.SunriseEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SunriseRepository extends CrudRepository<SunriseEntity, Long> {
    //Optional<SunriseEntity> findByCitiesAndDate(City city, LocalDate date);
    Optional<SunriseEntity> findByCityAndDate(City city, LocalDate localDate);
}
