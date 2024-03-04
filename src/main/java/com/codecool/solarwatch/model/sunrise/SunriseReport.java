package com.codecool.solarwatch.model.sunrise;

import java.time.LocalDate;

public record SunriseReport(String city, String sunrise, LocalDate date) {
}
