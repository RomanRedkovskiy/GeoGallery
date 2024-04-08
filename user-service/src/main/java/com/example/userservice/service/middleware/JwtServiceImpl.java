package com.example.userservice.service.middleware;

import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.TransformationProvider;
import com.example.userservice.util.jwt.JwtData;
import com.example.userservice.util.jwt.JwtParsingException;
import com.example.userservice.util.jwt.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.JwtUtils;

import java.util.Optional;

import static utils.JwtUtils.isTokenExpired;
import static utils.JwtUtils.parseToken;


@Service
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;

    @Autowired
    public JwtServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final static String prefix = "Bearer ";
    private final static int MILLISECONDS_IN_SECONDS = 1000;
    private final static int SECONDS_IN_MINUTE = 60;
    private final static int MINUTES_IN_HOUR = 60;

    @Override
    public String generateUserRegistrationHeader(String id) {
        String jwtToken = generateAuthorizationHeader(Role.USER);
        userRepository.addUserJwtToDatabase(id, jwtToken);
        return jwtToken;
    }

    @Override
    public String generateUserLoginHeader(String id) {
        String jwtToken;
        if (isUserAdmin(id)) {
            jwtToken = generateAuthorizationHeader(Role.ADMIN);
            userRepository.addUserJwtToDatabase(id, jwtToken);
            return jwtToken;
        }
        jwtToken = generateAuthorizationHeader(Role.USER);
        userRepository.addUserJwtToDatabase(id, jwtToken);
        return jwtToken;
    }

    @Override
    public String generateAuthorizationHeader(Role role) {

        return prefix + JwtUtils.generateToken(role.getString(),
                5 * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECONDS); //5 hours
    }

    @Override
    public Optional<JwtData> parseHeader(String header) {
        if (!header.startsWith(prefix)) {
            return Optional.empty();
        }
        String token = header.substring(prefix.length());
        Claims tokenBody = parseToken(token);
        if (tokenBody == null) {
            return Optional.empty();
        }
        JwtData jwtData = new JwtData();
        try {
            jwtData.setExpired(isTokenExpired(tokenBody.getExpiration()));
            jwtData.setRole(Role.fromString(tokenBody.getSubject()));
        } catch (JwtParsingException e) {
            return Optional.empty();
        }
        return Optional.of(jwtData);
    }

    private Boolean isUserAdmin(String id) {
        return TransformationProvider.handleFuture(userRepository.isUserAdmin(id));
    }

}
