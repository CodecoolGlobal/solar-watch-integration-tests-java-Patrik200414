package com.codecool.solarwatch.model.sunrise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SunriseResult (String sunrise){
}
