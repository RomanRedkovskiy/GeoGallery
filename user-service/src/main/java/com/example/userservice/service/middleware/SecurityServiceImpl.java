package com.example.userservice.service.middleware;

import com.example.userservice.util.jwt.JwtData;
import com.example.userservice.util.jwt.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    private final JwtService jwtService;

    @Autowired
    public SecurityServiceImpl(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean hasRole(String header) {
        Set<Role> roles = new HashSet<>(Set.of(Role.ADMIN, Role.USER));
        return wasDesiredRoleFound(header, roles);
    }

    @Override
    public boolean hasAdmin(String header) {
        Set<Role> roles = Collections.singleton(Role.ADMIN);
        return wasDesiredRoleFound(header, roles);
    }

    private boolean wasDesiredRoleFound(String header, Set<Role> roles) {
        Optional<JwtData> jwtOptional = jwtService.parseHeader(header);
        if (jwtOptional.isEmpty()) {
            return false;
        }
        JwtData jwtData = jwtOptional.get();
        for (Role role : roles) {
            if (jwtData.getRole() == role) {
                return true;
            }
        }
        return false;
    }
}
