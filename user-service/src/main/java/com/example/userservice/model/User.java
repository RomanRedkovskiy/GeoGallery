package com.example.userservice.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class User {

    private String login;

    private String password;

    private String name;

    private ArrayList<ImageBind> images;
}
