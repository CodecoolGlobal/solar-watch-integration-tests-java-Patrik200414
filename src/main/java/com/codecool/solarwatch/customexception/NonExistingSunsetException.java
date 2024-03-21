package com.codecool.solarwatch.customexception;

public class NonExistingSunsetException extends RuntimeException{
    public NonExistingSunsetException() {
        super("Non existing sunset exception!");
    }
}
