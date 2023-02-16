package com.example.registration;

import com.example.registration.entity.User;
import com.example.registration.model.UserDto;
import com.example.registration.repository.UserRepository;
import com.example.registration.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationApplicationTests {

    String url = "http://localhost:8080/";
    User user;
    UserDto userDto;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        user = new User(null, "test@gmail.com", "test", "mic", "lt");
        userDto = UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Test
    void getRegister_shouldReturn_signup_form_HTML_page() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(url + "register")
                        //.accept(MediaType.APPLICATION_JSON)
                        //.contentType(MediaType.APPLICATION_JSON)
                        //.content(objectMapper.writeValueAsString(userDto)))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void createUser_shouldReturn_statusCode_Ok() throws Exception {

        ResultActions result = mockMvc.perform(post(url + "process_register")
                        .param("email", "test@email.com")
                        .param("password", "test")
                        .param("firstName", "mario")
                        .param("lastName", "rossi")
                        )
                .andExpect(status().isCreated())
                .andDo(print());
        userRepository.deleteById(userRepository.findByEmail("test@email.com").getId());
    }

    @Test
    void createUser_shouldReturn_statusCode_Conflict_userEmailAlreadyExist() throws Exception {
        //save another user with same e-mail before the post req
        User user1 = userRepository.save(new User(null, "test@email.com","test","mario", "rossi"));

        ResultActions result = mockMvc.perform(post(url + "process_register")
                        .param("email", "test@email.com")
                        .param("password", "test")
                        .param("firstName", "mario")
                        .param("lastName", "rossi")
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andDo(print());
        userRepository.deleteById(userRepository.findByEmail(user1.getEmail()).getId());
    }
}
