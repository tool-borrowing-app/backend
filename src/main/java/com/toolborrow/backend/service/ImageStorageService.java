package com.toolborrow.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final Bucket bucket;

    private static final String FIREBASE_STORAGE_URL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";

    public List<String> uploadBase64Images(List<String> base64Images, String folder) {
        List<String> urls = new ArrayList<>();

        if (base64Images == null) {
            return urls;
        }

        for (String base64 : base64Images) {
            if (base64 == null || base64.isBlank()) {
                continue;
            }

            String[] parts = base64.split(",");
            String meta = parts.length > 1 ? parts[0] : "";
            String dataPart = parts.length > 1 ? parts[1] : parts[0];

            byte[] bytes = Base64.getDecoder().decode(dataPart);

            String contentType = "image/jpeg";
            String extension = ".jpg";
            if (meta.contains("image/png")) {
                contentType = "image/png";
            } else if (meta.contains("image/webp")) {
                contentType = "image/webp";
            }

            String objectName = folder + "/" + UUID.randomUUID() + extension;
            Blob blob = bucket.create(objectName, bytes, contentType);

            String encodedObjectName = URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8);

            String publicUrl = String.format(
                    FIREBASE_STORAGE_URL,
                    bucket.getName(),
                    encodedObjectName
            );

            urls.add(publicUrl);
        }

        return urls;
    }
}

