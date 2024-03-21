package com.codecool.solarwatch.model.dto.openweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoCode(String name, double lat, double lon, String state, String country) {
}
