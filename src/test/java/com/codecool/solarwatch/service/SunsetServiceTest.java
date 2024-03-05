package com.codecool.solarwatch.service;

import com.codecool.solarwatch.custom_exception.NonExistingCityException;
import com.codecool.solarwatch.custom_exception.NonExistingSunriseException;
import com.codecool.solarwatch.custom_exception.NonExistingSunsetException;
import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.model.entities.sunrise.SunriseEntity;
import com.codecool.solarwatch.model.entities.sunset.SunsetEntity;
import com.codecool.solarwatch.model.sunrise.*;
import com.codecool.solarwatch.model.sunset.*;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SunsetServiceTest {

    private RestTemplate restTemplateMock;
    private SunsetRepository sunsetRepositoryMock;
    private CityRepository cityRepositoryMock;

    @BeforeEach
    void CreateMockDependencies(){
        restTemplateMock = mock(RestTemplate.class);
        sunsetRepositoryMock = mock(SunsetRepository.class);
        cityRepositoryMock = mock(CityRepository.class);
    }

    @Test
    void getSunrise_SunriseRepositoryShouldReturnEmptyOptional_RestTemplateShouldReturnExpectedSunrise_ShouldReturnSunriseReportWithLosAngeles6AMCurrentDate() {
        //arrange
        LocalDate expectedDate = LocalDate.now();
        City expectedCity = new City(
                "Los Angeles",
                "California",
                "USA",
                1,
                1,
                new ArrayList<>(),
                new ArrayList<>()
        );
        String expectedSunset = "6 AM";

        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", expectedCity.getLatitude(), expectedCity.getLongitude(), expectedDate);

        when(sunsetRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());
        when(restTemplateMock.getForObject(url, Sunset.class)).thenReturn(
                new Sunset(
                        new SunsetResult(expectedSunset)
                )
        );

        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepositoryMock);

        //act
        SunsetReport result = sunsetService.getSunset(expectedCity, expectedDate);
        SunsetReport expected = new SunsetReport(expectedCity.getName(),expectedSunset, expectedDate);

        //assert
        assertEquals(expected, result);
    }


    @Test
    void getSunrise_SunriseRepositoryShouldReturnSunsetEntity_ShouldReturnSunriseReportWithLosAngeles6AMCurrentDate() {
        //arrange
        LocalDate expectedDate = LocalDate.now();
        City expectedCity = new City(
                "Los Angeles",
                "California",
                "USA",
                1,
                1,
                new ArrayList<>(),
                new ArrayList<>()
        );
        String expectedSunset = "6 AM";
        SunsetEntity expectedSunriseEntity = new SunsetEntity(
                expectedSunset,
                expectedDate,
                expectedCity
        );

        when(sunsetRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.of(expectedSunriseEntity));

        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepositoryMock);

        //act
        SunsetReport result = sunsetService.getSunset(expectedCity, expectedDate);
        SunsetReport expected = new SunsetReport(expectedCity.getName(),expectedSunset, expectedDate);

        //assert
        assertEquals(expected, result);
    }

    @Test
    void getSunrise_SunriseRepositoryShouldReturnEmptyOptional_RestTemplateShouldReturnNull_ShouldThrowNonExistingSunriseException() {
        //arrange
        LocalDate expectedDate = LocalDate.now();
        City expectedCity = new City(
                "Los Angeles",
                "California",
                "USA",
                1,
                1,
                new ArrayList<>(),
                new ArrayList<>()
        );

        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", expectedCity.getLatitude(), expectedCity.getLongitude(), expectedDate);

        when(sunsetRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());
        when(restTemplateMock.getForObject(url, Sunset.class)).thenReturn(null);

        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingSunsetException.class, () ->  sunsetService.getSunset(expectedCity, expectedDate));
    }


    @Test
    void createSunrise_CityRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingCityException() {
        //arrange
        LocalDate expectedDate = LocalDate.now();
        String expectedSunset = "6 AM";
        SunsetCreationDTO sunsetCreationDTO = new SunsetCreationDTO(expectedDate, expectedSunset, 1);

        when(cityRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingCityException.class, () -> sunsetService.createSunset(sunsetCreationDTO));
    }

    @Test
    void updateSunrise_SunriseRepositoryShouldReturnOptionalOfSunriseEntity_CityRepositoryShouldReturnEmptyOptional_ShouldThrowError() {
        LocalDate expectedDate = LocalDate.now();
        String expectedSunset = "6 AM";
        long expectedId = 1;

        SunsetUpdateDTO sunsetUpdateDTO = new SunsetUpdateDTO(
                expectedDate,
                expectedSunset,
                1
        );


        when(cityRepositoryMock.findById(sunsetUpdateDTO.cityId())).thenReturn(Optional.empty());
        when(sunsetRepositoryMock.findById(expectedId)).thenReturn(
                Optional.of(
                        new SunsetEntity()
                )
        );

        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingCityException.class, () -> sunsetService.updateSunset(1, sunsetUpdateDTO));
    }


    @Test
    void updateSunrise_SunriseRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingSunriseException() {
        LocalDate expectedDate = LocalDate.now();
        String expectedSunrise = "6 AM";
        long expectedId = 1;

        SunsetUpdateDTO sunsetUpdateDTO = new SunsetUpdateDTO(
                expectedDate,
                expectedSunrise,
                1
        );

        when(sunsetRepositoryMock.findById(expectedId)).thenReturn(Optional.empty());

        SunsetService sunsetService = new SunsetService(sunsetRepositoryMock, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingSunsetException.class, () -> sunsetService.updateSunset(1, sunsetUpdateDTO));
    }
}