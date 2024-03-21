package com.codecool.solarwatch.service;

import com.codecool.solarwatch.customexception.AlreadyExistingCityException;
import com.codecool.solarwatch.customexception.InvalidCityParameterException;
import com.codecool.solarwatch.customexception.NonExistingCityException;
import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.model.open_weather.GeoCode;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CityService {
    private final String API_KEY = "8c6b4b059c7dd13410f3cde43a7ca8c6";
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(CityService.class);
    private final CityRepository cityRepository;

    private final SunriseRepository sunriseRepository;

    private final SunsetRepository sunsetRepository;
    @Autowired
    public CityService(RestTemplate restTemplate, CityRepository cityRepository, SunriseRepository sunriseRepository, SunsetRepository sunsetRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
        this.sunriseRepository = sunriseRepository;
        this.sunsetRepository = sunsetRepository;
    }

    public City getCity(String city) throws InvalidCityParameterException{
        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", city, API_KEY);
        Optional<City> optionalCity = cityRepository.findByName(city);
        if(optionalCity.isPresent()){
            return optionalCity.get();
        }
        GeoCode[] cities = restTemplate.getForObject(url, GeoCode[].class);
        if(cities.length > 0){
            GeoCode cityResponse = cities[0];
            City newCity = createCityEntity(cityResponse);
            cityRepository.save(newCity);

            logger.info("Response from API: " + newCity);
            return newCity;
        }
        logger.error("Something went wrong!");
        throw new InvalidCityParameterException(city);
    }

    @Transactional
    public void saveCity(GeoCode city){
        System.out.println(city);
        Optional<City> searchedCity = cityRepository.findByName(city.name());

        if(searchedCity.isPresent()){
            throw new AlreadyExistingCityException(city.name());
        }

        City newCity = createCityEntity(city);
        cityRepository.save(newCity);
    }

    @Transactional
    public void updateCity(long cityId, GeoCode updatedCity){
        Optional<City> searchedCity = cityRepository.findById(cityId);

        if(searchedCity.isEmpty()){
            throw new NonExistingCityException(cityId);
        }

        City city = createCityEntity(updatedCity);
        city.setId(cityId);

        cityRepository.save(city);
    }

    @Transactional
    public void deleteCity(long id){
        Optional<City> searchedCity = cityRepository.findById(id);

        if(searchedCity.isEmpty()){
            throw new NonExistingCityException(id);
        }

        City city = searchedCity.get();

        sunriseRepository.deleteAll(city.getSunrise());
        sunsetRepository.deleteAll(city.getSunset());
        cityRepository.delete(city);
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
