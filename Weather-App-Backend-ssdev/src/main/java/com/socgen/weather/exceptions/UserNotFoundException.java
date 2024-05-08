package com.socgen.weather.exceptions;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {  // Constructor with a custom message
        super(message);
    }
}
