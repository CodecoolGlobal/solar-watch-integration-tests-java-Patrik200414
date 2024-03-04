package com.codecool.solarwatch.service;

import com.codecool.solarwatch.custom_exception.NonExistingCityException;
import com.codecool.solarwatch.custom_exception.NonExistingSunsetException;
import com.codecool.solarwatch.custom_exception.UnableToSaveSunrise;
import com.codecool.solarwatch.custom_exception.UnableToSaveSunsetException;
import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.model.entities.sunset.SunsetEntity;
import com.codecool.solarwatch.model.open_weather.GeoCode;
import com.codecool.solarwatch.model.sunset.Sunset;
import com.codecool.solarwatch.model.sunset.SunsetCreationDTO;
import com.codecool.solarwatch.model.sunset.SunsetReport;
import com.codecool.solarwatch.model.sunset.SunsetUpdateDTO;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SunsetService {

    private final static Logger logger = LoggerFactory.getLogger(SunriseService.class);
    private final SunsetRepository sunsetRepository;
    private final RestTemplate restTemplate;

    private final CityRepository cityRepository;

    @Autowired
    public SunsetService(SunsetRepository sunsetRepository, RestTemplate restTemplate, CityRepository cityRepository) {
        this.sunsetRepository = sunsetRepository;
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public SunsetReport getSunset(City city, LocalDate date){
        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s",city.getLatitude(),city.getLongitude(),date);

        Optional<SunsetEntity> optionalSunset = sunsetRepository.findByCityAndDate(city, date);

        if(optionalSunset.isPresent()){
            SunsetEntity sunset =  optionalSunset.get();
            return new SunsetReport(city.getName(), sunset.getSunset(),date);
        }

        Sunset sunsetData = restTemplate.getForObject(url, Sunset.class);

        if(sunsetData == null){
            throw new NonExistingSunsetException();
        }

        logger.info("Response from API: " + sunsetData);
        SunsetEntity sunsetEntity = createSunsetEntity(city, date, sunsetData);
        sunsetRepository.save(sunsetEntity);
        return new SunsetReport(city.getName(), sunsetData.results().sunset(), date);
    }

    @Transactional
    public void createSunset(SunsetCreationDTO sunsetCreationDTO){
        Optional<City> searchedCity = cityRepository.findById(sunsetCreationDTO.cityId());

        if(searchedCity.isEmpty()){
            throw new NonExistingCityException(sunsetCreationDTO.cityId());
        }

        City city = searchedCity.get();

        SunsetEntity sunset = new SunsetEntity(
                sunsetCreationDTO.sunset(),
                sunsetCreationDTO.date(),
                city
        );

        SunsetEntity sunsetResult = sunsetRepository.save(sunset);

        if(sunsetResult == null){
            throw new UnableToSaveSunsetException();
        }

    }

    @Transactional
    public void updateSunset(long id, SunsetUpdateDTO sunsetUpdateDTO){
        Optional<SunsetEntity> searchedSunset = sunsetRepository.findById(id);
        Optional<City> searchedCity = cityRepository.findById(sunsetUpdateDTO.cityId());

        if(searchedSunset.isEmpty()){
            throw new NonExistingSunsetException();
        }

        if(searchedCity.isEmpty()){
            throw new NonExistingCityException(sunsetUpdateDTO.cityId());
        }

        SunsetEntity sunsetEntity = new SunsetEntity(
                id,
                sunsetUpdateDTO.sunset(),
                sunsetUpdateDTO.date(),
                searchedCity.get()
        );

        sunsetRepository.save(sunsetEntity);
    }

    @Transactional
    public void deleteSunset(long id){
        sunsetRepository.deleteById(id);
    }


    private static SunsetEntity createSunsetEntity(City city, LocalDate date, Sunset sunsetData) {
        SunsetEntity sunsetEntity = new SunsetEntity();
        sunsetEntity.setSunset(sunsetData.results().sunset());
        sunsetEntity.setCity(city);
        sunsetEntity.setDate(date);
        return sunsetEntity;
    }


    private City createCityEntity(GeoCode city) {
        City newCity = new City();
        newCity.setName(city.name());
        newCity.setState(city.state());
        newCity.setCountry(city.country());
        newCity.setLatitude(city.lat());
        newCity.setLongitude(city.lon());
        return newCity;
    }
}
