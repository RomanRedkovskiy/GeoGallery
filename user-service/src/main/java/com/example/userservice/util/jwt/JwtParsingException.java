package com.example.userservice.util.jwt;

public class JwtParsingException extends RuntimeException{

    public JwtParsingException(String message) {
        super(message);
    }
}
