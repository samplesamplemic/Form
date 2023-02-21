package com.example.registration.repository;

import com.example.registration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //@Query("SELECT u FROM User u WHERE u.email = ?1") // '?' =  in place of a single parameter that the service expects as input, prompting you for it;
    //written with this nomenclature jpa recognizes the scope and auto-write the Query
    public User findByEmail(String email);

    public void deleteByEmail(String email);

}
