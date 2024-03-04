package com.codecool.solarwatch.custom_exception;

public class UnableToSaveSunrise extends RuntimeException{
    public UnableToSaveSunrise() {
        super("Unable to save new sunrise!");
    }
}
