package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunriseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SunriseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private SunriseRepository sunriseRepository;

    @WithMockUser
    @Test
    void getSunrise_RepositoriesMockedOut_APIShouldReturnTheExpectedStringInJSONFormat() throws Exception {

        LocalDate expectedDate = LocalDate.of(2024, 3, 4);
        String expectedCityName = "Los Angeles";
        String expectedSunrise = "2:15:26 PM";
        String expected = String.format("{\"city\":\"%s\",\"sunrise\":\"%s\",\"date\":\"%s\"}", expectedCityName, expectedSunrise, expectedDate);
        City expectedCity = new City(
                expectedCityName,
                "California",
                "USA",
                -118.243683,
                34.052235,
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(cityRepository.findByName(expectedCityName)).thenReturn(Optional.of(expectedCity));
        when(sunriseRepository.findByCityAndDate(expectedCity, expectedDate)).thenReturn(Optional.empty());

        this.mockMvc.perform(get(String.format("/api/sunrise?city=%s&date=%s", expectedCityName, expectedDate)))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));

    }


}