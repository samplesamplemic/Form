package com.example.registration.security;

import com.example.registration.security.authentication.CustomFailureAuthentication;
import com.example.registration.service.CustomUserDetailsService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Log4j2
public class securityConfig {

//    @Autowired
//    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //allow multiple session for the same user
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());

        http.headers().frameOptions().disable(); //disable security to access the h2-database console for testing
        http.csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try {
                        auth
                                .requestMatchers("/users").authenticated()
                                .anyRequest().permitAll()
                                .and()
                                .formLogin()
                                .loginPage("/login").permitAll()
                                .failureHandler(authenticationFailureHandler())
                                .usernameParameter("email")
                                .defaultSuccessUrl("/users")
                                .permitAll()
                                .and()
                                .logout().logoutSuccessUrl("/").permitAll();
                        //.and()
                        //.rememberMe().userDetailsService(new CustomUserDetailsService())
                        //.key("uniqueAndSecret");// remember for number of seconds
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
//      to notified Spring Security session registry when the session is destroyed
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomFailureAuthentication();
    }

}

