package dev.yol.final_project_back.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

@Configuration
public class FirebaseConfig {
    @Value("classpath:/private-key.json")
    private Resource privateKey;

    /* @Value("${firebase.storage.bucket}")
    private String storageBucket; */


    @Bean
public FirebaseApp firebaseApp() throws IOException {
    // Esto buscará private-key.json dentro de src/main/resources/
    ClassPathResource serviceAccount = new ClassPathResource("private-key.json");

    if (FirebaseApp.getApps().isEmpty()) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .setStorageBucket("final-project-20571.firebasestorage.app") //¿Esto puede estar público?
                .build();
        return FirebaseApp.initializeApp(options);
    } else {
        return FirebaseApp.getInstance();
    }
}

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
