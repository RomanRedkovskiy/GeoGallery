package com.example.userservice.dto.imageDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ImageUrlBindDto {

    private String imageUrl;

    private List<Double> position;
}
