package com.codecool.solarwatch.model.dto.sunrise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseResult (String sunrise){
}
