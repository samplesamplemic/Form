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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class RegistrationApplicationTests {

    String url = "http://localhost:8080/";
    String email = "test@email.com";
    String password = "test";
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

    @AfterEach
    void cleanSetUp() {
        if(userRepository.findByEmail(email) != null){
            userRepository.deleteById(userRepository.findByEmail(email).getId());
        }
    }

    @Test
    void getRegister_shouldReturn_statusOk_and_signup_form_HTML_page() throws Exception {
        ResultActions result = mockMvc.perform(get(url + "register"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void createUser_shouldReturn_statusCode_Ok_and_matchNewUser() throws Exception {
        //to form-urlEncoded
        ResultActions result = mockMvc.perform(post(url + "process_register")
                        .param("email", email)
                        .param("password", "test")
                        .param("firstName", "mario")
                        .param("lastName", "rossi")
                )
                .andExpect(status().isCreated())
                .andDo(print());
        UserDto userToMatch = userService.findUser(email);
        assertThat(userToMatch).isNotNull();
        assertEquals("mario", userToMatch.getFirstName());
    }

    @Test
    void createUser_shouldReturn_statusCodeConflict_userAlreadyExistException() throws Exception {
        //save another user with same e-mail before the post req
        User user1 = userRepository.save(new User(null, email, "test", "mario", "rossi"));

        ResultActions result = mockMvc.perform(post(url + "process_register")
                        .param("email", email)
                        .param("password", "test")
                        .param("firstName", "mario")
                        .param("lastName", "rossi")
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void findAllUser_shouldReturn_listOfUsersInHTMLPage_and_statusCodeOk_after_loginAuthentication() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pswEncoded = encoder.encode(password);

        User user1 = userRepository.save(new User(null, email, pswEncoded, "mario", "rossi"));
        User user2 = userRepository.save(new User(null, "test2@email", pswEncoded, "giovanni", "verdi"));
        MvcResult result = mockMvc.perform(formLogin().user("email",email).password(password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(authenticated().withUsername(email))
                .andDo(print())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        mockMvc.perform(get(url+"users").session(session))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
