package com.example.userservice.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationDto {

    private String login;

    private String password;

    private String name;

}
