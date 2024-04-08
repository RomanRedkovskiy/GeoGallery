package com.example.userservice.util;

import com.example.userservice.dto.imageDto.ImagePathBindDto;
import com.example.userservice.model.ImageBind;
import com.example.userservice.util.exceptions.AuthenticationExecutionException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TransformationProvider {

    public static <T> T handleFuture(CompletableFuture<T> future) {
        future.exceptionally(throwable -> {
            throw new AuthenticationExecutionException("Authentication failed. Try again later.");
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AuthenticationExecutionException(e.getMessage());
        }
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static List<ImagePathBindDto> imageBindsToImagePathBinds(List<ImageBind> imageBinds) {
        return imageBinds.stream()
                .map(imageBind -> {
                    ImagePathBindDto imagePathBindDto = new ImagePathBindDto();
                    imagePathBindDto.setImagePath(imageBind.getImagePath());
                    imagePathBindDto.setPosition(Arrays.asList(imageBind.getLatitude(), imageBind.getLongitude()));
                    return imagePathBindDto;
                })
                .collect(Collectors.toList());
    }
}
