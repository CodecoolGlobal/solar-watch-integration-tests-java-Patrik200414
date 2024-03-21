package com.codecool.solarwatch.model.sunset;

import java.time.LocalDate;

public record SunsetCreationDTO(LocalDate date, String sunset, long cityId) {
}
