package com.example.userservice.repository;

import com.example.userservice.dto.commentDto.CommentDto;
import com.example.userservice.model.Comment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ImageRepository {

    void addCommentToDatabase(String filePath, String id, Comment comment);

    void deleteCommentFromDatabase(String filePath, String id);

    CompletableFuture<List<CommentDto>> getImageCommentsFromDatabase(String id);

}
