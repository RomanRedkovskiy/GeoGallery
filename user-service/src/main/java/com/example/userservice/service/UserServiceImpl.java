package com.example.userservice.service;

import com.example.userservice.dto.imageDto.ImagePathBindDto;
import com.example.userservice.dto.imageDto.ImageUrlBindDto;
import com.example.userservice.dto.userDto.UserCredentialsDto;
import com.example.userservice.dto.userDto.UserGeneralDto;
import com.example.userservice.dto.userDto.UserIndexedDto;
import com.example.userservice.dto.userDto.UserRegistrationDto;
import com.example.userservice.model.ImageBind;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.TransformationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserIndexedDto processUserRegistration(UserRegistrationDto user) {

        CompletableFuture<UserIndexedDto> registeredUserFuture = userRepository.registerUser(user);
        UserIndexedDto registeredUser = TransformationProvider.handleFuture(registeredUserFuture);

        userRepository.addUserToDatabase(registeredUser.getId(),
                new UserGeneralDto(registeredUser.getName(), null));
        return registeredUser;
    }

    @Override
    public UserIndexedDto processUserLogin(UserCredentialsDto user) {

        CompletableFuture<UserIndexedDto> loginUserFuture = userRepository.loginUser(user);
        return TransformationProvider.handleFuture(loginUserFuture);
    }

    //add image path and its geo position to Firebase Database
    //add image to Firebase Storage
    @Override
    public UserIndexedDto processImageAddition(String id, List<Double> position, MultipartFile file) {

        String filePath = "Images/" + generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));
        userRepository.addFileToStorage(file, filePath);
        ImageBind imageBind = new ImageBind(filePath, position.get(0), position.get(1));

        UserIndexedDto user = retrieveUserFromDatabase(id);

        user.getImages().add(imageBind);

        userRepository.addUserToDatabase(id, new UserGeneralDto(user.getName(), user.getImages()));
        return user;
    }

    //for admin purposes
    @Override
    public List<UserIndexedDto> getUsers() {
        return retrieveAllUsersFromDatabase();
    }

    @Override
    public List<ImageUrlBindDto> getUserImageUrlBinds(String id) {
        return imagePathBindsToImageUrlBinds(getUserImagePathBinds(id));
    }

    @Override
    public List<ImagePathBindDto> getUserImagePathBinds(String id) {

        UserIndexedDto user = retrieveUserFromDatabase(id);
        return TransformationProvider.imageBindsToImagePathBinds(user.getImages());
    }

    //process image delete
    @Override
    public void processImageDelete(String id, String filePath) {
        deleteFileFromStorage(filePath); //delete image from Firebase Storage
        deleteImageComments(filePath); //delete image comments from Firebase Database
        deleteUserFileFromDatabase(id, filePath); //delete image bind from Firebase Database
    }

    //to retrieve image in React component
    @Override
    public String getFileUrlFromStorage(String filePath) {
        return userRepository.getFileUrlFromStorage(filePath);
    }

    @Override
    public void processUserDelete(String id) {
        //we cannot delete admin user programmatically
        if (retrieveIsAdminFromDatabase(id)) {
            throw new UnsupportedOperationException("Attempt to delete admin user");
        }
        userRepository.deleteUserJwtFromDatabase(id); //delete user's jwt token in Firebase Database;
        List<String> filePaths = retrieveUserFilePaths(id); //retrieve all image paths for user
        filePaths.forEach(filePath -> {
            userRepository.deleteImageCommentsFromDatabase(filePath); //delete all comments for current image
            userRepository.deleteFileFromStorage(filePath); //delete image from Firebase Storage
        });
        userRepository.deleteUserFromDatabase(id); //delete User from Firebase Database
        userRepository.deleteUserFromAuthenticate(id); //delete User from Firebase Authentication
    }

    private void deleteFileFromStorage(String filePath) {
        userRepository.deleteFileFromStorage(filePath);
    }

    private void deleteImageComments(String imagePath) {
        userRepository.deleteImageCommentsFromDatabase(imagePath);
    }

    private void deleteUserFileFromDatabase(String id, String filePath) {
        UserIndexedDto user = retrieveUserFromDatabase(id);
        user.getImages().removeIf(imageBind -> imageBind.getImagePath().equals(filePath));
        userRepository.addUserToDatabase(id, new UserGeneralDto(user.getName(), user.getImages()));
    }

    private String generateUniqueFileName(String filePath) {
        int dotIndex = filePath.indexOf('.');
        String fileName = (dotIndex != -1) ? filePath.substring(0, dotIndex) : filePath;
        String randomString = TransformationProvider.generateRandomUUID();

        return fileName + "_" + randomString;
    }

    private List<ImageUrlBindDto> imagePathBindsToImageUrlBinds(List<ImagePathBindDto> imagePathBindDtos) {
        return imagePathBindDtos.stream()
                .map(imagePathBindDto -> {
                    ImageUrlBindDto imageUrlBindDto = new ImageUrlBindDto();
                    imageUrlBindDto.setPosition(imagePathBindDto.getPosition());
                    imageUrlBindDto.setImageUrl(getFileUrlFromStorage(imagePathBindDto.getImagePath()));
                    return imageUrlBindDto;
                })
                .collect(Collectors.toList());
    }

    private List<String> retrieveUserFilePaths(String id) {
        return retrieveUserFromDatabase(id).getImages().stream().map(ImageBind::getImagePath).toList();
    }

    private Boolean retrieveIsAdminFromDatabase(String id) {
        CompletableFuture<Boolean> isAdminFuture = userRepository.isUserAdmin(id);
        return TransformationProvider.handleFuture(isAdminFuture);
    }


    private UserIndexedDto retrieveUserFromDatabase(String id) {
        CompletableFuture<UserIndexedDto> retrievedUserFuture = userRepository.getUserFromDatabase(id);
        return TransformationProvider.handleFuture(retrievedUserFuture);
    }

    private List<UserIndexedDto> retrieveAllUsersFromDatabase() {
        CompletableFuture<List<UserIndexedDto>> retrievedUsersFuture = userRepository.getAllUsersFromDatabase();
        return TransformationProvider.handleFuture(retrievedUsersFuture);
    }
}
