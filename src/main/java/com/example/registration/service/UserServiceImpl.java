package com.example.registration.service;

import com.example.registration.entity.User;
import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDto userDto) {
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
            return newUser;
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

    public User findUser(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }
}
