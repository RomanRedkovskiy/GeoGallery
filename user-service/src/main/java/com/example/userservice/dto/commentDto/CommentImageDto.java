package com.example.userservice.dto.commentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentImageDto {

    private String comment;

    private String imagePath;
}
