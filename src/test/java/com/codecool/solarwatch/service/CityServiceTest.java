package com.codecool.solarwatch.service;

import com.codecool.solarwatch.customexception.AlreadyExistingCityException;
import com.codecool.solarwatch.customexception.InvalidCityParameterException;
import com.codecool.solarwatch.customexception.NonExistingCityException;
import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.model.open_weather.GeoCode;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CityServiceTest {
    private RestTemplate restTemplateMock;
    private CityRepository cityRepositoryMock;
    private SunriseRepository sunriseRepositoryMock;
    private SunsetRepository sunsetRepositoryMock;
    private final String API_KEY = "8c6b4b059c7dd13410f3cde43a7ca8c6";

    @BeforeEach
    void CreateMockDependencies(){
        restTemplateMock = mock(RestTemplate.class);
        cityRepositoryMock = mock(CityRepository.class);
        sunriseRepositoryMock = mock(SunriseRepository.class);
        sunsetRepositoryMock = mock(SunsetRepository.class);
    }

    @Test
    void getCity_RepositoryShouldReturnEmptyOptional_RestTemplateMockReturnsExpectedCity_ShouldReturnCityWithNameLosAngele() {
        //arrange
        GeoCode expectedCity = new GeoCode(
                "Los Angeles",
                1,
                1,
                "California",
                "USA"
        );

        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", expectedCity.name(), API_KEY);

        when(restTemplateMock.getForObject(url, GeoCode[].class)).thenReturn(new GeoCode[]{expectedCity});
        when(cityRepositoryMock.findByName(expectedCity.name())).thenReturn(Optional.empty());
        CityService cityService = new CityService(restTemplateMock, cityRepositoryMock, sunriseRepositoryMock, sunsetRepositoryMock);

        //act
        City result = cityService.getCity(expectedCity.name());
        City expected = new City(
                expectedCity.name(),
                expectedCity.state(),
                expectedCity.country(),
                expectedCity.lon(),
                expectedCity.lat(),
                List.of(),
                List.of()
        );
        //assert
        assertEquals(expected, result);
    }


    @Test
    void getCity_RepositoryShouldReturnExpectedCityLosAngele_RestTemplateMocked_ShouldReturnCityWithNameLosAngele() {
        //arrange
        GeoCode expectedCity = new GeoCode(
                "Los Angeles",
                1,
                1,
                "California",
                "USA"
        );

        City expected = new City(
                expectedCity.name(),
                expectedCity.state(),
                expectedCity.country(),
                expectedCity.lon(),
                expectedCity.lat(),
                List.of(),
                List.of()
        );

        when(cityRepositoryMock.findByName(expectedCity.name())).thenReturn(Optional.of(expected));
        CityService cityService = new CityService(restTemplateMock, cityRepositoryMock, sunriseRepositoryMock, sunsetRepositoryMock);

        //act
        City result = cityService.getCity(expectedCity.name());
        //assert
        assertEquals(expected, result);
    }


    @Test
    void getCity_RepositoryShouldReturnEmptyOptional_RestTemplateMockReturnsEmptyArray_ShouldThrowInvalidCityParameterException() {
        //arrange
        GeoCode expectedCity = new GeoCode(
                "NonExisting city",
                -100,
                -100,
                "Unknown state",
                "Unknown country"
        );

        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", expectedCity.name(), API_KEY);

        when(restTemplateMock.getForObject(url, GeoCode[].class)).thenReturn(new GeoCode[]{});
        when(cityRepositoryMock.findByName(expectedCity.name())).thenReturn(Optional.empty());
        CityService cityService = new CityService(
                restTemplateMock,
                cityRepositoryMock,
                sunriseRepositoryMock,
                sunsetRepositoryMock
        );

        //assert
        assertThrows(InvalidCityParameterException.class, () -> cityService.getCity(expectedCity.name()));
    }

    @Test
    void saveCity_WithCityRepositoryMockedShouldReturnAnOptionaWithACityObject_ShouldThrewAlreadyExistingCityException() {
        //arrange
        GeoCode expectedCity = new GeoCode(
                "Los Angeles",
                1,
                1,
                "California",
                "USA"
        );

        when(cityRepositoryMock.findByName(expectedCity.name())).thenReturn(Optional.of(new City()));


        CityService cityService = new CityService(
                restTemplateMock,
                cityRepositoryMock,
                sunriseRepositoryMock,
                sunsetRepositoryMock
        );

        assertThrows(AlreadyExistingCityException.class, () -> cityService.saveCity(expectedCity));
    }

    @Test
    void updateCity_WithCityRepositoryMockedShouldReturnEmptyOptional_ShouldThrowNonExistingCityException() {
        //arrange
        GeoCode updatedCityValues = new GeoCode(
                "Los Angeles",
                1,
                1,
                "California",
                "USA"
        );

        when(cityRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        CityService cityService = new CityService(
                restTemplateMock,
                cityRepositoryMock,
                sunriseRepositoryMock,
                sunsetRepositoryMock
        );

        assertThrows(NonExistingCityException.class, () -> cityService.updateCity(1, updatedCityValues));
    }

    @Test
    void deleteCity() {
        //arrange
        when(cityRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        CityService cityService = new CityService(
                restTemplateMock,
                cityRepositoryMock,
                sunriseRepositoryMock,
                sunsetRepositoryMock
        );

        assertThrows(NonExistingCityException.class, () -> cityService.deleteCity(1));
    }
}