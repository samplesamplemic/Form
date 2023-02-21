package com.example.registration.service;

import com.example.registration.entity.User;
import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService<UserDto> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) throws UserAlreadyExistsException {
        log.info("User try to sign up...");
        String email = userDto.getEmail();
        if (userRepository.findByEmail(email) == null) {
            log.info(String.format("User with email: %s not found, proceeding to its creation", email));

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            log.info("Password created successfully");
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            userDto.setPassword(encodedPassword);

            User newUser = User.builder()
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .firstName(userDto.getFirstName().toLowerCase())
                    .lastName(userDto.getLastName().toLowerCase())
                    .build();
            userRepository.save(newUser);
            log.info(String.format("New user created, id: %s", newUser.getId()));

            return userDto;
        } else {
            UserAlreadyExistsException err = new UserAlreadyExistsException();
            log.info("Exception throws: " + err.getMessage());
            throw err;
        }
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserDto> usersList = userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), null, user.getFirstName(), user.getLastName())).toList();

        return usersList;
    }

    @Override
    public UserDto findUser(String email) {
        User user = userRepository.findByEmail(email);
        log.info("Requested user: "+user.toString());
        UserDto userDtoToReturn = UserDto.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return userDtoToReturn;
    }
}
