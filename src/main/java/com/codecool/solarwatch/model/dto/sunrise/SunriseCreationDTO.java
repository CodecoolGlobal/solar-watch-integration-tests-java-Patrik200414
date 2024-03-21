package com.codecool.solarwatch.model.dto.sunrise;

import java.time.LocalDate;

public record SunriseCreationDTO(LocalDate date, String sunrise, long cityId) {
}
