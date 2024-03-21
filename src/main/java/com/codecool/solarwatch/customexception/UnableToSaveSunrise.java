package com.codecool.solarwatch.customexception;

public class UnableToSaveSunrise extends RuntimeException{
    public UnableToSaveSunrise() {
        super("Unable to save new sunrise!");
    }
}
