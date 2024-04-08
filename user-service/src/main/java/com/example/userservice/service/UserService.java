package com.example.userservice.service;

import com.example.userservice.dto.imageDto.ImagePathBindDto;
import com.example.userservice.dto.imageDto.ImageUrlBindDto;
import com.example.userservice.dto.userDto.UserCredentialsDto;
import com.example.userservice.dto.userDto.UserIndexedDto;
import com.example.userservice.dto.userDto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserIndexedDto processUserRegistration(UserRegistrationDto user);

    UserIndexedDto processUserLogin(UserCredentialsDto user);

    UserIndexedDto processImageAddition(String id, List<Double> position, MultipartFile file);

    List<UserIndexedDto> getUsers();

    List<ImageUrlBindDto> getUserImageUrlBinds(String id);

    List<ImagePathBindDto> getUserImagePathBinds(String id);

    void processImageDelete(String id, String filePath);

    String getFileUrlFromStorage(String filePath);

    void processUserDelete(String id);

}
