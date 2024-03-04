package com.codecool.solarwatch.model.sunrise;

import com.codecool.solarwatch.model.open_weather.GeoCode;

import java.time.LocalDate;

public record SunriseCreationDTO(LocalDate date, String sunrise, long cityId) {
}
