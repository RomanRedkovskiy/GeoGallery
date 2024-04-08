package com.example.userservice.dto.commentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentIdImageDto {

    private String commentId;

    private String imagePath;
}
