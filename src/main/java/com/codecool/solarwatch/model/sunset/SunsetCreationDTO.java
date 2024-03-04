package com.codecool.solarwatch.model.sunset;

import com.codecool.solarwatch.model.open_weather.GeoCode;

import java.time.LocalDate;

public record SunsetCreationDTO(LocalDate date, String sunset, long cityId) {
}
