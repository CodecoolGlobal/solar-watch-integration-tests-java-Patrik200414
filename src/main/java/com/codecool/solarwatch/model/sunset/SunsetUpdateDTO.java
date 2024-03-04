package com.codecool.solarwatch.model.sunset;

import java.time.LocalDate;

public record SunsetUpdateDTO(LocalDate date, String sunset, long cityId) {
}
