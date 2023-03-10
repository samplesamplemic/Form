package com.example.registration.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

}
