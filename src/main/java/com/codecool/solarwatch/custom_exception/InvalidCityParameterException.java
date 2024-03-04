package com.codecool.solarwatch.custom_exception;

public class InvalidCityParameterException extends RuntimeException{
    public InvalidCityParameterException(String city) {
        super(String.format("The city '%s' is not exists!", city));
    }
}
