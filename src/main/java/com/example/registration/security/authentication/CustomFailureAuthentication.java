package com.example.registration.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class CustomFailureAuthentication implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        String err = exception.getMessage();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        log.info(String.format("A failed login with email: %s. Reason: %s. %s", email, err, date.format(new Date())));

        String redirectUrl = request.getContextPath() + "/login?error";
        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //response.setHeader("Location", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
