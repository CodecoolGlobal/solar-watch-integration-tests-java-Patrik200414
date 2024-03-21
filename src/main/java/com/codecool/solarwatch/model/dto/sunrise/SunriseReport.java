package com.codecool.solarwatch.model.dto.sunrise;

import java.time.LocalDate;

public record SunriseReport(String city, String sunrise, LocalDate date) {
}
