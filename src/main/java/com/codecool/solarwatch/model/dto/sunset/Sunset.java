package com.codecool.solarwatch.model.dto.sunset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Sunset(SunsetResult results) {
}
