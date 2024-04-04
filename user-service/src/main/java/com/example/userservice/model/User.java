package com.example.userservice.model;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String login;

    private String password;

    private String name;

    private List<PhotoBind> images;
}
