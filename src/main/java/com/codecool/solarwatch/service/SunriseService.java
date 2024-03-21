package com.codecool.solarwatch.service;

import com.codecool.solarwatch.customexception.NonExistingCityException;
import com.codecool.solarwatch.customexception.NonExistingSunriseException;
import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.model.entity.sunrise.SunriseEntity;
import com.codecool.solarwatch.model.dto.sunrise.Sunrise;
import com.codecool.solarwatch.model.dto.sunrise.SunriseCreationDTO;
import com.codecool.solarwatch.model.dto.sunrise.SunriseReport;
import com.codecool.solarwatch.model.dto.sunrise.SunriseUpdateDTO;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SunriseService {
    private final RestTemplate restTemplate;
    private final static Logger logger = LoggerFactory.getLogger(SunriseService.class);
    private final SunriseRepository sunriseRepository;

    private final CityRepository cityRepository;

    @Autowired
    public SunriseService(RestTemplate restTemplate, SunriseRepository sunriseRepository, CityRepository cityRepository) {
        this.restTemplate = restTemplate;
        this.sunriseRepository = sunriseRepository;
        this.cityRepository = cityRepository;
    }

    public SunriseReport getSunrise(City city, LocalDate date){
        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s",city.getLatitude(),city.getLongitude(),date);

        Optional<SunriseEntity> optionalSunrise = sunriseRepository.findByCityAndDate(city ,date);
        if(optionalSunrise.isPresent()){
            SunriseEntity sunrise =  optionalSunrise.get();
            return new SunriseReport(city.getName(),sunrise.getSunrise(), sunrise.getDate());
        }

        Sunrise sunriseData = restTemplate.getForObject(url, Sunrise.class);

        if(sunriseData == null){
            throw new NonExistingSunriseException();
        }
        logger.info("Response from API: " + sunriseData);
        SunriseEntity sunriseEntity = createSunriseEntity(city, date, sunriseData);
        sunriseRepository.save(sunriseEntity);
        return new SunriseReport(city.getName(), sunriseData.results().sunrise(), date);
    }

    @Transactional
    public void createSunrise(SunriseCreationDTO sunriseCreationDTO){
        Optional<City> searchedCity = cityRepository.findById(sunriseCreationDTO.cityId());

        if(searchedCity.isEmpty()){
            throw new NonExistingCityException(sunriseCreationDTO.cityId());
        }

        City city = searchedCity.get();

        SunriseEntity sunriseEntity = new SunriseEntity(
                sunriseCreationDTO.sunrise(),
                sunriseCreationDTO.date(),
                city
        );

        sunriseRepository.save(sunriseEntity);

    }

    @Transactional
    public void updateSunrise(long id, SunriseUpdateDTO sunriseUpdateDTO){
        Optional<SunriseEntity> searchedSunrise = sunriseRepository.findById(id);
        Optional<City> searchedCity = cityRepository.findById(sunriseUpdateDTO.cityId());
        if(searchedSunrise.isEmpty()){
            throw new NonExistingSunriseException();
        }

        if(searchedCity.isEmpty()){
            throw new NonExistingCityException(sunriseUpdateDTO.cityId());
        }

        SunriseEntity sunrise = new SunriseEntity(
                id,
                sunriseUpdateDTO.sunrise(),
                sunriseUpdateDTO.date(),
                searchedCity.get()
        );

        sunriseRepository.save(sunrise);
    }


    @Transactional
    public void deleteSunrise(long id){
        sunriseRepository.deleteById(id);
    }


    private SunriseEntity createSunriseEntity(City city, LocalDate date, Sunrise sunriseData) {
        SunriseEntity sunriseEntity = new SunriseEntity();
        sunriseEntity.setSunrise(sunriseData.results().sunrise());
        sunriseEntity.setCity(city);
        sunriseEntity.setDate(date);
        return sunriseEntity;
    }

}
