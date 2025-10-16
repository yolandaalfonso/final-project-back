package dev.yol.final_project_back.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yol.final_project_back.globals.InvalidLoginCredentialsException;

@RestController
@RequestMapping(path="${api-endpoint}/auth")
public class AuthController {
    private final FirebaseAuthClient firebaseAuthClient;

    public AuthController(FirebaseAuthClient firebaseAuthClient) {
        this.firebaseAuthClient = firebaseAuthClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody FirebaseSignInRequest request) {
        try {
            System.out.println("🟢 Login request recibido para: " + request.email());
            FirebaseSignInResponse response = firebaseAuthClient.login(request.email(), request.password());
            return ResponseEntity.ok(response);
        } catch (InvalidLoginCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body(Map.of("error", "Invalid login credentials"));
        }
        
    }
}
