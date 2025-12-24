package com.toolborrow.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account-json}")
    private String serviceAccountJson;

    @Bean
    public Storage firebaseStorage() throws IOException {
        InputStream serviceAccountStream = new ByteArrayInputStream(serviceAccountJson.getBytes());

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

        return StorageOptions.newBuilder()
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