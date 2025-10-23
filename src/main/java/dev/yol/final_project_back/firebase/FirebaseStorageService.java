package dev.yol.final_project_back.firebase;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();

        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

        // Generar URL pública
        return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), fileName);
    }
}
