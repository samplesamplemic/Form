package com.example.registration.repository;

import com.example.registration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1") // '?' =  in place of a single parameter that the service expects as input, prompting you for it;
    public User findByEmail(String email);
}
