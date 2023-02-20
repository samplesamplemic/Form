package com.example.registration;

import com.example.registration.entity.User;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationUnitTest {

    String email = "test@mail.com";
    User user;
    UserDto userDto;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        user = new User(null, email, "test", "mic", "lt");
        userDto = UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Test
    void Create_return_OneUser() {
        lenient().when(userRepository.findByEmail(anyString())).thenReturn(null);
        lenient().when(userRepository.save(user)).thenReturn(user);
        User userToMatch = userService.createUser(userDto);
        System.out.println(userToMatch);
        assertThat(userToMatch).isNotNull();
    }

    @Test
    void query_findByEmail_userRepository() {
        when(userRepository.findByEmail(email)).thenReturn(user);
        User userToMatch = userService.findUser(email);
        assertThat(userToMatch.getFirstName()).isEqualTo("mic");
    }

}
