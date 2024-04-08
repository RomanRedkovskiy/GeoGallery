package com.example.userservice.service;

import com.example.userservice.dto.imageDto.ImageDetailsDto;

public interface ImageService {

    ImageDetailsDto getImageDetails(String filePath);

    void addImageComment(String imagePath, String comment);

    void deleteImageComment(String imagePath, String commentId);

}
