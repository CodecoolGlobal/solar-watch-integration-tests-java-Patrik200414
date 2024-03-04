package com.codecool.solarwatch.custom_exception;

public class AlreadyExistingCityException extends RuntimeException{
    public AlreadyExistingCityException(String name) {
        super(String.format("City with name: '%s' already exists!", name));
    }
}
