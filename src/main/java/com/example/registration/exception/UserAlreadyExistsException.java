package com.example.registration.exception;

public class UserAlreadyExistsException extends CustomException {

    public UserAlreadyExistsException() {
        super("User already registered");
    }
}
