package com.example.userservice.service;

import com.example.userservice.dto.commentDto.CommentDto;
import com.example.userservice.dto.imageDto.ImageDetailsDto;
import com.example.userservice.model.Comment;
import com.example.userservice.repository.ImageRepository;
import com.example.userservice.util.TransformationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final UserService userService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    @Override
    public ImageDetailsDto getImageDetails(String filePath) {
        return new ImageDetailsDto(
                userService.getFileUrlFromStorage(filePath),
                retrieveImageCommentList(filePath));
    }

    @Override
    public void addImageComment(String imagePath, String commentStr) {
        Comment comment = new Comment(commentStr, localDateTimeToFormattedStr());
        imageRepository.addCommentToDatabase(imagePath, TransformationProvider.generateRandomUUID(), comment);
    }

    @Override
    public void deleteImageComment(String imagePath, String commentId) {
        imageRepository.deleteCommentFromDatabase(imagePath, commentId);
    }


    private String localDateTimeToFormattedStr() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy HH:mm:ss");
        return now.format(formatter);
    }

    private List<CommentDto> retrieveImageCommentList(String filePath) {
        CompletableFuture<List<CommentDto>> userCommentsFuture = imageRepository.getImageCommentsFromDatabase(filePath);
        List<CommentDto> userComments = TransformationProvider.handleFuture(userCommentsFuture);
        userComments.sort((comment1, comment2) -> comment2.getDate().compareTo(comment1.getDate()));
        return userComments;
    }
}
