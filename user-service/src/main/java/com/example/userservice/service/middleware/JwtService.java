package com.example.userservice.service.middleware;

import com.example.userservice.util.jwt.JwtData;
import com.example.userservice.util.jwt.Role;

import java.util.Optional;

public interface JwtService {

    String generateUserRegistrationHeader(String id);

    String generateUserLoginHeader(String id);

    String generateAuthorizationHeader(Role role);

    Optional<JwtData> parseHeader(String header);

}
