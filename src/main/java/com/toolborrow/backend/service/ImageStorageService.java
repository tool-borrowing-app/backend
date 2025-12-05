package com.toolborrow.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    private final Bucket bucket;

    @Value("${firebase.public-url-prefix:https://storage.googleapis.com}")
    private String publicUrlPrefix;

    public List<String> uploadBase64Images(List<String> base64Images, String folder) {
        System.out.println("Called uploadBase64Images using publicUrlPrefix: " + publicUrlPrefix);

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
            if (meta.contains("image/png")) {
                contentType = "image/png";
            } else if (meta.contains("image/webp")) {
                contentType = "image/webp";
            }

            String objectName = folder + "/" + UUID.randomUUID();
            Blob blob = bucket.create(objectName, bytes, contentType);

            String url = publicUrlPrefix + "/" + bucket.getName() + "/" + blob.getName();
            urls.add(url);
        }

        return urls;
    }
}

