package com.example.registration.service;

import com.example.registration.exception.UserAlreadyExistsException;

import java.util.List;

public interface UserService<T> {

    T createUser(T userDto) throws UserAlreadyExistsException;

    List<T> findAllUsers();

    T findUser(String email);
}
