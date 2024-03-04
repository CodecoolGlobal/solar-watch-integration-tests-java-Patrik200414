package com.codecool.solarwatch.custom_exception;

public class NonExistingCityException extends RuntimeException{
    public NonExistingCityException(long id) {
        super(String.format("City with id: %s doesn't exists!", id));
    }
}
