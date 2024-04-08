package com.example.userservice.dto.userDto;

import com.example.userservice.model.ImageBind;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserIndexedDto {

    private String id;

    private String name;

    private List<ImageBind> images;
}
