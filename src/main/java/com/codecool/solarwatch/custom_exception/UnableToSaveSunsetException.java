package com.codecool.solarwatch.custom_exception;

public class UnableToSaveSunsetException extends RuntimeException{
    public UnableToSaveSunsetException() {
        super("Unable to save sunset!");
    }
}
