package com.codecool.solarwatch.model.dto.sunset;

import java.time.LocalDate;

public record SunsetReport(String city, String sunset, LocalDate date) {
}
