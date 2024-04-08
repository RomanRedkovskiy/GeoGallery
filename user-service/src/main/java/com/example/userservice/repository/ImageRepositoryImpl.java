package com.example.userservice.repository;

import com.example.userservice.dto.commentDto.CommentDto;
import com.example.userservice.model.Comment;
import com.example.userservice.util.exceptions.RealtimeDatabaseException;
import com.google.firebase.database.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ImageRepositoryImpl implements ImageRepository {

    @Override
    public void addCommentToDatabase(String filePath, String id, Comment comment) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Comments").child(filePath);
        try {
            databaseRef.child(id).setValueAsync(comment);
        } catch (DatabaseException e) {
            throw new RealtimeDatabaseException(e.getMessage());
        }
    }

    @Override
    public void deleteCommentFromDatabase(String filePath, String id) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Comments").child(filePath);
        try {
            databaseRef.child(id).removeValueAsync();
        } catch (DatabaseException e) {
            throw new RealtimeDatabaseException(e.getMessage());
        }
    }

    //CompletableFuture will be handled in UserService
    @Override
    public CompletableFuture<List<CommentDto>> getImageCommentsFromDatabase(String filePath) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Comments");
        CompletableFuture<List<CommentDto>> future = new CompletableFuture<>();

        databaseRef.child(filePath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CommentDto> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    // Extract the values from the snapshot
                    String id = commentSnapshot.getKey();
                    String text = commentSnapshot.child("text").getValue(String.class);
                    String date = commentSnapshot.child("date").getValue(String.class);
                    comments.add(new CommentDto(id, text, date));
                }
                future.complete(comments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RealtimeDatabaseException("Error retrieving data: " + databaseError.getMessage()));
            }
        });

        return future;
    }
}
