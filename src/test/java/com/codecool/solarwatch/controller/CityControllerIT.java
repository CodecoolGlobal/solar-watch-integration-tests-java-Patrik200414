package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityRepository cityRepository;


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createCity_WithUser_RoleUSER_ShouldReturnStatusCode403Forbidden() throws Exception {
        String requestBody = "{\n" +
                "    \"name\": \"Budapest\",\n" +
                "    \"lat\": 3.6,\n" +
                "    \"lon\": 2.5,\n" +
                "    \"state\": \"Pest county\",\n" +
                "    \"country\": \"Hungary\"\n" +
                "}";


        this.mockMvc.perform(post("/api/city").contentType(APPLICATION_JSON_UTF8)
                .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void createCity_WithUser_RoleADMIN_ShouldReturnStatusOK() throws Exception {
        String requestBody = "{\n" +
                "    \"name\": \"Budapest\",\n" +
                "    \"lat\": 3.6,\n" +
                "    \"lon\": 2.5,\n" +
                "    \"state\": \"Pest county\",\n" +
                "    \"country\": \"Hungary\"\n" +
                "}";


        this.mockMvc.perform(post("/api/city").contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andExpect(status().isOk()).andExpect(content()
                        .string("City is saved!"));
    }

    @Test
    void updateCity() {
    }

    @Test
    void deleteCity() {
    }
}