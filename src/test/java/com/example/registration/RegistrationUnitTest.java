package com.example.registration;

import com.example.registration.entity.User;
import com.example.registration.exception.UserAlreadyExistsException;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Log4j2
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
    void CreateUserService_ShouldSave_newUser_and_Return_OneUserDto() throws UserAlreadyExistsException {
        lenient().when(userRepository.findByEmail(anyString())).thenReturn(null);
        lenient().when(userRepository.save(user)).thenReturn(user);
        UserDto userToMatch = userService.createUser(userDto);
        System.out.println(userToMatch);
        assertThat(userToMatch).isNotNull();
    }

    @Test
    void findAllUser_shouldReturn_listOfAllUsers(){
        User user1 = new User(null, email, "test", "mario", "rossi");
        User user2 = new User(null, "test2@email", "test", "giovanni", "verdi");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserDto> usersListToMatch= userService.findAllUsers();
        assertThat(usersListToMatch.size()==2);
        assertThat(usersListToMatch.get(0).getFirstName().equalsIgnoreCase("mario"));
        assertThat(usersListToMatch.get(1).getFirstName().equalsIgnoreCase("giovanni"));
    }

    @Test
    void findByEmail_queryRepository_ShouldReturn_userDtoSearchedByEmail() {
        when(userRepository.findByEmail(email)).thenReturn(user);
        UserDto userToMatch = userService.findUser(email);
        assertThat(userToMatch.getFirstName()).isEqualToIgnoringCase("mic");
    }

    @Test
    void userAlreadyExistsException_should_return_Throwable(){
        UserAlreadyExistsException err = new UserAlreadyExistsException();
        try {
            log.info("Error: "+err);
            throw err;
        } catch (Throwable e){
            assertThat(e).isEqualTo(err);
            assertThat(e.getMessage()).isEqualTo("User already registered");
        }
    }
}
