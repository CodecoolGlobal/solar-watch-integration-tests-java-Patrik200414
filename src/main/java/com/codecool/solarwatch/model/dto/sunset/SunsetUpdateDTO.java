package com.codecool.solarwatch.model.dto.sunset;

import java.time.LocalDate;

public record SunsetUpdateDTO(LocalDate date, String sunset, long cityId) {
}
