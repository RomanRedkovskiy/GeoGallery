package com.example.userservice.dto;

import com.example.userservice.model.PhotoBind;
import lombok.Data;

import java.util.List;

@Data
public class UserGeneralDto {

    private String name;

    private List<PhotoBind> images;
}
