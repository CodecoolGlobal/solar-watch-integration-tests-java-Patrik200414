package com.codecool.solarwatch.service;

import com.codecool.solarwatch.customexception.NonExistingCityException;
import com.codecool.solarwatch.customexception.NonExistingSunsetException;
import com.codecool.solarwatch.model.dto.sunset.*;
import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.model.entity.sunset.SunsetEntity;
import com.codecool.solarwatch.model.dto.sunrise.Sunrise;
import com.codecool.solarwatch.model.sunset.*;
import com.codecool.solarwatch.repository.CityRepository;
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
    private SunsetRepository sunsetRepository;
    private CityRepository cityRepositoryMock;

    @BeforeEach
    void CreateMockDependencies(){
        restTemplateMock = mock(RestTemplate.class);
        sunsetRepository = mock(SunsetRepository.class);
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

        when(sunsetRepository.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());
        when(restTemplateMock.getForObject(url, Sunset.class)).thenReturn(
                new Sunset(
                        new SunsetResult(expectedSunset)
                )
        );

        SunsetService sunsetService = new SunsetService(sunsetRepository, restTemplateMock, cityRepositoryMock);

        //act
        SunsetReport result = sunsetService.getSunset(expectedCity, expectedDate);
        SunsetReport expected = new SunsetReport(expectedCity.getName(), expectedSunset, expectedDate);

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
        SunsetEntity expectedSunsetEntity = new SunsetEntity(
                expectedSunset,
                expectedDate,
                expectedCity
        );

        when(sunsetRepository.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.of(expectedSunsetEntity));

        SunsetService sunsetService = new SunsetService(sunsetRepository, restTemplateMock, cityRepositoryMock);

        //act
        SunsetReport result = sunsetService.getSunset(expectedCity, expectedDate);
        SunsetReport expected = new SunsetReport(expectedCity.getName(), expectedSunset, expectedDate);

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

        when(sunsetRepository.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());
        when(restTemplateMock.getForObject(url, Sunrise.class)).thenReturn(null);

        SunsetService sunsetService = new SunsetService(sunsetRepository, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingSunsetException.class, () ->  sunsetService.getSunset(expectedCity, expectedDate));
    }


    @Test
    void createSunrise_CityRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingCityException() {
        //arrange
        LocalDate expectedDate = LocalDate.now();
        String expectedSunset = "6 AM";
        SunsetCreationDTO sunsetCreationDTO = new SunsetCreationDTO(expectedDate, expectedSunset, 1);

        when(cityRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        SunsetService sunsetService = new SunsetService(sunsetRepository, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingCityException.class, () -> sunsetService.createSunset(sunsetCreationDTO));
    }

    @Test
    void updateSunrise_SunriseRepositoryShouldReturnOptionalOfSunriseEntity_CityRepositoryShouldReturnEmptyOptional_ShouldThrowError() {
        LocalDate expectedDate = LocalDate.now();
        String expectedSunset = "6 AM";

        SunsetUpdateDTO sunsetUpdateDTO = new SunsetUpdateDTO(
                expectedDate,
                expectedSunset,
                1
        );


        when(cityRepositoryMock.findById(sunsetUpdateDTO.cityId())).thenReturn(Optional.empty());
        when(sunsetRepository.findById(1L)).thenReturn(
                Optional.of(
                        new SunsetEntity()
                )
        );

        SunsetService sunsetService = new SunsetService(sunsetRepository, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingCityException.class, () -> sunsetService.updateSunset(1, sunsetUpdateDTO));
    }


    @Test
    void updateSunrise_SunriseRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingSunriseException() {
        LocalDate expectedDate = LocalDate.now();
        String expectedSunset = "6 AM";

        SunsetUpdateDTO sunsetUpdateDTO = new SunsetUpdateDTO(
                expectedDate,
                expectedSunset,
                1
        );

        when(sunsetRepository.findById(1L)).thenReturn(Optional.empty());

        SunsetService sunsetService = new SunsetService(sunsetRepository, restTemplateMock, cityRepositoryMock);

        assertThrows(NonExistingSunsetException.class, () -> sunsetService.updateSunset(1, sunsetUpdateDTO));
    }
}