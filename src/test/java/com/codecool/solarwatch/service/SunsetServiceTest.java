package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.model.entities.sunset.SunsetEntity;
import com.codecool.solarwatch.model.sunset.Sunset;
import com.codecool.solarwatch.model.sunset.SunsetReport;
import com.codecool.solarwatch.model.sunset.SunsetResult;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SunsetServiceTest {

    /*
    @Test
    void getSunset_WithSunriseRepositoryMockedOutAndReturnsAValidSunriseEntity_ShouldReturnSunriseReportForDallasCity(){
        LocalDate expectedDate = LocalDate.now();

        City expectedCity = new City();
        expectedCity.setLatitude(1);
        expectedCity.setLongitude(1);
        expectedCity.setCountry("USA");
        expectedCity.setState("Texas");
        expectedCity.setName("Dallas");

        SunsetEntity expectedSunsetEntity = new SunsetEntity();
        expectedSunsetEntity.setDate(expectedDate);
        expectedSunsetEntity.setSunset("6:30 PM");
        expectedSunsetEntity.setCity(expectedCity);

        SunsetRepository sunsetRepositoryMock = mock(SunsetRepository.class);
        when(sunsetRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.of(expectedSunsetEntity));

        RestTemplate restTemplate = new RestTemplate();

        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplate, cityRepository);

        SunsetReport result = sunsetService.getSunset(expectedCity, expectedDate);
        SunsetReport expected = new SunsetReport(expectedCity.getName(), expectedSunsetEntity.getSunset(), expectedDate);

        assertEquals(expected, result);
    }

    @Test
    void getSunset_WithSunriseRepositoryMockedOutAndReturnsAnEmptyOptional_RestTemplateIsMockedOutAndReturnsTheSunriseOfDallas_ShouldReturnSunriseReportForDallasCity(){
        LocalDate expectedDate = LocalDate.now();

        City expectedCity = new City();
        expectedCity.setLatitude(1);
        expectedCity.setLongitude(1);
        expectedCity.setCountry("USA");
        expectedCity.setState("Texas");
        expectedCity.setName("Dallas");

        SunsetEntity expectedSunsetEntity = new SunsetEntity();
        expectedSunsetEntity.setDate(expectedDate);
        expectedSunsetEntity.setSunset("6:30 PM");
        expectedSunsetEntity.setCity(expectedCity);

        SunsetRepository sunsetRepositoryMock = mock(SunsetRepository.class);
        when(sunsetRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());

        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s",expectedCity.getLatitude(),expectedCity.getLongitude(),expectedDate);

        SunsetResult sunsetResult = new SunsetResult(expectedSunsetEntity.getSunset());
        Sunset expectedSunset = new Sunset(sunsetResult);

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        when(restTemplateMock.getForObject(url, Sunset.class)).thenReturn(expectedSunset);


        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepository);

        SunsetReport result = sunsetService.getSunset(expectedCity, expectedDate);
        SunsetReport expected = new SunsetReport(expectedCity.getName(), expectedSunsetEntity.getSunset(), expectedDate);

        assertEquals(expected, result);
    }

     */
}