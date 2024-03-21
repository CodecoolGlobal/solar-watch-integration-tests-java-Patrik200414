package com.codecool.solarwatch.customexception;

public class NonExistingUserException extends RuntimeException{
    public NonExistingUserException(long id) {
        super(String.format("User with id: %s is not exists in the system!", id));
    }
}
