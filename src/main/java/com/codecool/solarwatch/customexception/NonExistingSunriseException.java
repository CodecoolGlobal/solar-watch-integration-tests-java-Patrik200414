package com.codecool.solarwatch.customexception;

public class NonExistingSunriseException extends RuntimeException{
    public NonExistingSunriseException() {
        super("Non existing sunrise data!");
    }
}
