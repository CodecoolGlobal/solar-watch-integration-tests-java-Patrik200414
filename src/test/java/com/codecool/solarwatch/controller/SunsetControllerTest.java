package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.entities.city.City;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void createSunrise_UserWithUSERRole_ShouldReturn401UnAuthorizeError() throws Exception {
        String createdSunrise = "{\"date\": \"2024-02-24\",\"sunset\": \"4 PM\",\"cityId\": 1}";

        this.mockMvc.perform(post("/api/sunset")
                .contentType(MediaType.APPLICATION_JSON).content(createdSunrise)).andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createSunrise_UserWithADMINRole_ShouldReturn201Created() throws Exception {
        String createdSunrise = "{\"date\": \"2024-02-24\",\"sunset\": \"4 PM\",\"cityId\": 1}";
        City expectedCity = new City(
                "Los Angeles",
                "California",
                "USA",
                -118.243683,
                34.052235,
                new ArrayList<>(),
                new ArrayList<>()
        );
        when(cityRepository.findById(1L)).thenReturn(Optional.of(expectedCity));

        this.mockMvc.perform(post("/api/sunset")
                .contentType(MediaType.APPLICATION_JSON).content(createdSunrise)).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createSunrise_UserWithADMINRoleCreatesASunriseWithNonExistingCityId_ShouldReturn409AndNonExistingCityErrorMessage() throws Exception {
        String createdSunrise = "{\"date\": \"2024-02-24\",\"sunset\": \"4 PM\",\"cityId\": 1}";
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc.perform(post("/api/sunset")
                        .contentType(MediaType.APPLICATION_JSON).content(createdSunrise)).andExpect(status().isConflict())
                .andExpect(content().string("City with id: 1 doesn't exists!"));
    }

}