package com.codecool.solarwatch.service;

import com.codecool.solarwatch.customexception.NonExistingCityException;
import com.codecool.solarwatch.customexception.NonExistingSunriseException;
import com.codecool.solarwatch.model.dto.sunrise.*;
import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.model.entity.sunrise.SunriseEntity;
import com.codecool.solarwatch.model.sunrise.*;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SunriseServiceTest {
    private RestTemplate restTemplateMock;
    private SunriseRepository sunriseRepositoryMock;
    private CityRepository cityRepositoryMock;

    @BeforeEach
    void CreateMockDependencies(){
        restTemplateMock = mock(RestTemplate.class);
        sunriseRepositoryMock = mock(SunriseRepository.class);
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
        String expectedSunrise = "6 AM";

        String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", expectedCity.getLatitude(), expectedCity.getLongitude(), expectedDate);

        when(sunriseRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());
        when(restTemplateMock.getForObject(url, Sunrise.class)).thenReturn(
                new Sunrise(
                        new SunriseResult(expectedSunrise)
                )
        );

        SunriseService sunriseService = new SunriseService(restTemplateMock, sunriseRepositoryMock, cityRepositoryMock);

        //act
        SunriseReport result = sunriseService.getSunrise(expectedCity, expectedDate);
        SunriseReport expected = new SunriseReport(expectedCity.getName(), expectedSunrise, expectedDate);

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
        String expectedSunrise = "6 AM";
        SunriseEntity expectedSunriseEntity = new SunriseEntity(
                expectedSunrise,
                expectedDate,
                expectedCity
        );

        when(sunriseRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.of(expectedSunriseEntity));

        SunriseService sunriseService = new SunriseService(restTemplateMock, sunriseRepositoryMock, cityRepositoryMock);

        //act
        SunriseReport result = sunriseService.getSunrise(expectedCity, expectedDate);
        SunriseReport expected = new SunriseReport(expectedCity.getName(), expectedSunrise, expectedDate);

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

        when(sunriseRepositoryMock.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());
        when(restTemplateMock.getForObject(url, Sunrise.class)).thenReturn(null);

        SunriseService sunriseService = new SunriseService(restTemplateMock, sunriseRepositoryMock, cityRepositoryMock);

        assertThrows(NonExistingSunriseException.class, () ->  sunriseService.getSunrise(expectedCity, expectedDate));
    }


    @Test
    void createSunrise_CityRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingCityException() {
        //arrange
        LocalDate expectedDate = LocalDate.now();
        String expectedSunrise = "6 AM";
        SunriseCreationDTO sunriseCreationDTO = new SunriseCreationDTO(expectedDate, expectedSunrise, 1);

        when(cityRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        SunriseService sunriseService = new SunriseService(restTemplateMock, sunriseRepositoryMock, cityRepositoryMock);

        assertThrows(NonExistingCityException.class, () -> sunriseService.createSunrise(sunriseCreationDTO));
    }

    @Test
    void updateSunrise_SunriseRepositoryShouldReturnOptionalOfSunriseEntity_CityRepositoryShouldReturnEmptyOptional_ShouldThrowError() {
        LocalDate expectedDate = LocalDate.now();
        String expectedSunrise = "6 AM";

        SunriseUpdateDTO sunriseUpdateDTO = new SunriseUpdateDTO(
            expectedDate,
            expectedSunrise,
            1,
            1
        );


        when(cityRepositoryMock.findById(sunriseUpdateDTO.cityId())).thenReturn(Optional.empty());
        when(sunriseRepositoryMock.findById(sunriseUpdateDTO.id())).thenReturn(
                Optional.of(
                        new SunriseEntity()
                )
        );

        SunriseService sunriseService = new SunriseService(restTemplateMock, sunriseRepositoryMock, cityRepositoryMock);

        assertThrows(NonExistingCityException.class, () -> sunriseService.updateSunrise(1, sunriseUpdateDTO));
    }


    @Test
    void updateSunrise_SunriseRepositoryShouldReturnEmptyOptional_ShouldThrowNonExistingSunriseException() {
        LocalDate expectedDate = LocalDate.now();
        String expectedSunrise = "6 AM";

        SunriseUpdateDTO sunriseUpdateDTO = new SunriseUpdateDTO(
                expectedDate,
                expectedSunrise,
                1,
                1
        );

        when(sunriseRepositoryMock.findById(sunriseUpdateDTO.id())).thenReturn(Optional.empty());

        SunriseService sunriseService = new SunriseService(restTemplateMock, sunriseRepositoryMock, cityRepositoryMock);

        assertThrows(NonExistingSunriseException.class, () -> sunriseService.updateSunrise(1, sunriseUpdateDTO));
    }
}