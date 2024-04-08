package com.example.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageBind {

    //path to image in Firebase Storage
    private String imagePath;

    private Double latitude;

    private Double longitude;
}
