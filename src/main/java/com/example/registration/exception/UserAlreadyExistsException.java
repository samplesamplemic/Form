package com.example.registration.exception;

public class UserAlreadyExistsException extends IllegalArgumentException {
    public UserAlreadyExistsException(String message) {super (message);}
}
