package com.codecool.solarwatch.model.sunset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunsetResult(String sunset) {
}
