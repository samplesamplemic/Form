package com.example.registration.service;

import com.example.registration.entity.User;
import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    public String createUser(UserDto userDto, BindingResult bindingResult) {
        if (userRepository.findByEmail(userDto.getEmail()) == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            userDto.setPassword(encodedPassword);
            User newUser = User.builder()
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .firstName(userDto.getFirstName().toLowerCase())
                    .lastName(userDto.getLastName().toLowerCase())
                    .build();
            userRepository.save(newUser);
            return "register_success";
        } else {
            // throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "User already registered");
            //ObjectError err = new ObjectError("form", "User already exists");
            //bindingResult.addError(err);
            //return "login";
            throw new UserAlreadyExistsException("User already registered");
        }
    }


    public List<User> findAllUser() {
        List<User> usersList = userRepository.findAll();
        return usersList;
    }
}
