package com.example.userservice.repository;

import com.example.userservice.dto.userDto.UserCredentialsDto;
import com.example.userservice.dto.userDto.UserGeneralDto;
import com.example.userservice.dto.userDto.UserIndexedDto;
import com.example.userservice.dto.userDto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {

    CompletableFuture<UserIndexedDto> registerUser(UserRegistrationDto user);

    CompletableFuture<UserIndexedDto> getUserFromDatabase(String id);

    //for admin purposes
    CompletableFuture<List<UserIndexedDto>> getAllUsersFromDatabase();

    CompletableFuture<Boolean> isUserAdmin(String id);

    CompletableFuture<UserIndexedDto> loginUser(UserCredentialsDto user);

    //to retrieve image by url in React
    String getFileUrlFromStorage(String filePath);

    //store user token in Firebase Database in format user.id -> user.jwt
    void addUserJwtToDatabase(String id, String jwt);

    void deleteUserJwtFromDatabase(String id);

    void deleteImageCommentsFromDatabase(String imagePath);

    void deleteUserFromDatabase(String id);

    void addUserToDatabase(String id, UserGeneralDto user);

    void addFileToStorage(MultipartFile file, String filePath);

    void deleteFileFromStorage(String filePath);

    void deleteUserFromAuthenticate(String id);

}
