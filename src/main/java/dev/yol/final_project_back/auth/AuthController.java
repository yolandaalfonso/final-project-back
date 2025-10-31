package dev.yol.final_project_back.auth;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yol.final_project_back.globals.InvalidLoginCredentialsException;
import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserMapper;
import dev.yol.final_project_back.user.UserRepository;
import dev.yol.final_project_back.user.dtos.UserResponseDTO;

@RestController
@RequestMapping(path="${api-endpoint}/auth")
public class AuthController {

    private final FirebaseAuthClient firebaseAuthClient;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(FirebaseAuthClient firebaseAuthClient, UserRepository userRepository, UserMapper userMapper) {
        this.firebaseAuthClient = firebaseAuthClient;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    @GetMapping("/current")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 👇 convierte automáticamente el UserEntity al DTO
        UserResponseDTO dto = userMapper.userEntityToUserResponseDto(currentUser);

        return ResponseEntity.ok(dto);
    }

}
