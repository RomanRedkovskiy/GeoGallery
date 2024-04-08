package com.example.userservice.service.middleware;

public interface SecurityService {

    boolean hasRole(String header);

    boolean hasAdmin(String header);

}