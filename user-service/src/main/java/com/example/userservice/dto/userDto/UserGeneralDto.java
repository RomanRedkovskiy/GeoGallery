package com.example.userservice.dto.userDto;

import com.example.userservice.model.ImageBind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneralDto {

    private String name;

    private List<ImageBind> images;
}
