package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SunsetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private SunsetRepository sunsetRepository;

    @Test
    @WithMockUser
    void getSunset_RepositoriesMockedOut_APIShouldReturnExpectedString() throws Exception {
        //Arrange
        LocalDate expectedDate = LocalDate.of(2024, 3, 4);
        String expectedCityName = "Los Angeles";
        String expectedSunset = "1:53:31 AM";
        String expected = String.format("{\"city\":\"%s\",\"sunset\":\"%s\",\"date\":\"%s\"}", expectedCityName, expectedSunset, expectedDate);
        City expectedCity = new City(
                expectedCityName,
                "California",
                "USA",
                -118.243683,
                34.052235,
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(cityRepository.findByName(expectedCityName)).thenReturn(Optional.empty());
        when(sunsetRepository.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(String.format("/api/sunset?city=%s&date=%s", expectedCityName, expectedDate)))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}