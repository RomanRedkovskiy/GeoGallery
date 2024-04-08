package com.example.userservice.rest.controller;

import com.example.userservice.dto.commentDto.CommentIdImageDto;
import com.example.userservice.dto.commentDto.CommentImageDto;
import com.example.userservice.dto.imageDto.ImageDetailsDto;
import com.example.userservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    //return image url and list of image comments
    @PreAuthorize("@securityService.hasRole(#header)")
    @PostMapping("/details")
    public ImageDetailsDto getImageDetails(@RequestHeader("Authorization") String header,
                                           @RequestBody String imagePath) {
        return imageService.getImageDetails(imagePath);
    }

    //add comment to image
    @PreAuthorize("@securityService.hasRole(#header)")
    @PostMapping("/comment")
    public void addImageComment(@RequestHeader("Authorization") String header,
                                @RequestBody CommentImageDto commentImageDto) {
        System.out.println(LocalDateTime.now());
        imageService.addImageComment(commentImageDto.getImagePath(), commentImageDto.getComment());
    }

    //delete comment from image
    @PreAuthorize("@securityService.hasRole(#header)")
    @DeleteMapping("/comment")
    public void deleteImageComment(@RequestHeader("Authorization") String header,
                                   @RequestBody CommentIdImageDto commentIdImageDto) {
        System.out.println(LocalDateTime.now());
        imageService.deleteImageComment(commentIdImageDto.getImagePath(), commentIdImageDto.getCommentId());
    }
}