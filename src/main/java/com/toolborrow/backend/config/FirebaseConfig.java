package com.toolborrow.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {


    @Bean
    public Storage firebaseStorage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("firebase-service-account.json").getInputStream()
        );
        return
                StorageOptions.newBuilder()
                        .setCredentials(credentials)
                        .build()
                        .getService();
    }

    @Bean
    public Bucket firebaseBucket(
            Storage storage,
            @Value("${firebase.bucket-name}") String bucketName
    ) {
        return storage.get(bucketName);
    }
}