package com.example.userservice.rest.controller;

import com.example.userservice.dto.imageDto.ImagePathBindDto;
import com.example.userservice.dto.imageDto.ImageUrlBindDto;
import com.example.userservice.dto.userDto.UserCredentialsDto;
import com.example.userservice.dto.userDto.UserIndexedDto;
import com.example.userservice.dto.userDto.UserRegistrationDto;
import com.example.userservice.service.UserService;
import com.example.userservice.service.middleware.JwtService;
import com.example.userservice.service.middleware.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService, SecurityService securityService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.securityService = securityService;
    }

    //get all users (for admin purposes)
    @PreAuthorize("@securityService.hasAdmin(#header)")
    @GetMapping
    public List<UserIndexedDto> getUsers(@RequestHeader("Authorization") String header) {
        return userService.getUsers();
    }

    //get urls and geo position for all photos
    @PreAuthorize("@securityService.hasRole(#header)")
    @GetMapping("/{id}/urls")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ImageUrlBindDto> getImageUrls(@RequestHeader("Authorization") String header, @PathVariable String id) {
        return userService.getUserImageUrlBinds(id);
    }

    //get paths and geo position for all photos
    @PreAuthorize("@securityService.hasRole(#header)")
    @GetMapping("/{id}/paths")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ImagePathBindDto> getUserPaths(@RequestHeader("Authorization") String header, @PathVariable String id) {
        return userService.getUserImagePathBinds(id);
    }

    //to choose component after authorization
    @GetMapping("/isAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean isAdmin(@RequestHeader("Authorization") String header) {
        //This method returns false if user isn't an admin
        //That's why @PreAuthorize annotation wasn't used here.
        return securityService.hasAdmin(header);
    }

    //add user in required Firebase services
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserIndexedDto addImage(HttpServletResponse response, @RequestBody UserRegistrationDto user) {
        UserIndexedDto userIndexedDto = userService.processUserRegistration(user);
        response.setHeader("Authorization", jwtService.generateUserRegistrationHeader(userIndexedDto.getId()));
        return userIndexedDto;
    }

    //user login
    @PostMapping("/login")
    public UserIndexedDto loginUser(HttpServletResponse response, @RequestBody UserCredentialsDto user) {
        UserIndexedDto userIndexedDto = userService.processUserLogin(user);
        response.setHeader("Authorization", jwtService.generateUserLoginHeader(userIndexedDto.getId()));
        return userIndexedDto;
    }

    //process image bind addition
    @PreAuthorize("@securityService.hasRole(#header)")
    @PostMapping("/{id}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public UserIndexedDto addImage(@RequestHeader("Authorization") String header, @PathVariable String id,
                                   @RequestParam("pos") List<Double> position, @RequestPart("file") MultipartFile file) {
        return userService.processImageAddition(id, position, file);
    }

    //process image bind delete
    @PreAuthorize("@securityService.hasRole(#header)")
    @DeleteMapping("/{id}/image")
    public void deleteImage(@RequestHeader("Authorization") String header,
                            @PathVariable String id, @RequestBody String filePath) {
        userService.processImageDelete(id, filePath);
    }

    @PreAuthorize("@securityService.hasAdmin(#header)")
    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("Authorization") String header,
                            @PathVariable String id) {
        userService.processUserDelete(id);
    }

}