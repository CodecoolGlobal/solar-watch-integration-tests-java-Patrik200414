package com.codecool.solarwatch.customexception;

public class AlreadyExistingCityException extends RuntimeException{
    public AlreadyExistingCityException(String name) {
        super(String.format("City with name: '%s' already exists!", name));
    }
}
