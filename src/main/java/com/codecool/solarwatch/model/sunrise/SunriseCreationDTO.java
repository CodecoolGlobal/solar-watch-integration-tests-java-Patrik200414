package com.codecool.solarwatch.model.sunrise;

import java.time.LocalDate;

public record SunriseCreationDTO(LocalDate date, String sunrise, long cityId) {
}
