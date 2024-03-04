package com.codecool.solarwatch.custom_exception;

public class NonExistingSunriseException extends RuntimeException{
    public NonExistingSunriseException() {
        super("Non existing sunrise data!");
    }
}
