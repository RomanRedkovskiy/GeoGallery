package com.example.userservice.dto.imageDto;

import com.example.userservice.dto.commentDto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDetailsDto {

    private String fileUrl;

    private List<CommentDto> comments;
 }
