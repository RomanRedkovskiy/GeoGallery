package com.example.userservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config}")
    String firebaseConfig;

    @Value("${project.id}")
    String projectId;

    @Bean
    public FirebaseApp configureFirebaseConnection() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("user-service/src/main/resources/" +
                "static/geogallery-ab822-firebase-adminsdk-s61m4-db46850697.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://geogallery-ab822-default-rtdb.firebaseio.com/")
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Storage storage() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(firebaseConfig);

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return storageOptions.getService();
    }
}
