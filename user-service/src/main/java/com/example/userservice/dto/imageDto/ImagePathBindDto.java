package com.example.userservice.dto.imageDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ImagePathBindDto {

    private String imagePath;

    private List<Double> position;

}
