package com.codecool.solarwatch.custom_exception;

public class NonExistingSunsetException extends RuntimeException{
    public NonExistingSunsetException() {
        super("Non existing sunset exception!");
    }
}
