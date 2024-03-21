package com.codecool.solarwatch.model.dto.sunrise;

import java.time.LocalDate;

public record SunriseUpdateDTO(LocalDate date, String sunrise, long cityId, long id) {
}
