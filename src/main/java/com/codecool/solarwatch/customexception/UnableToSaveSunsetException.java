package com.codecool.solarwatch.customexception;

public class UnableToSaveSunsetException extends RuntimeException{
    public UnableToSaveSunsetException() {
        super("Unable to save sunset!");
    }
}
