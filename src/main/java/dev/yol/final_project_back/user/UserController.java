package dev.yol.final_project_back.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yol.final_project_back.user.dtos.UserResponseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> {
                var profile = user.getProfile();
                return ResponseEntity.ok(new UserResponseDTO(
                    user.getId_user(),
                    user.getEmail(),
                    profile.getName(),
                    user.getUid(),
                    profile.getUserName(),
                    profile.getFirstSurname(),
                    profile.getBio(),
                    profile.getAvatar()
                ));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Nuevo endpoint para obtener usuario por UID (Firebase)
    @GetMapping("/byUid/{uid}")
    public ResponseEntity<UserResponseDTO> getUserByUid(@PathVariable String uid) {
        return userRepository.findByUid(uid)
            .map(user -> {
                var p = user.getProfile();
                return ResponseEntity.ok(new UserResponseDTO(
                    user.getId_user(),
                    user.getEmail(),
                    p.getName(),
                    user.getUid(),
                    p.getUserName(),
                    p.getFirstSurname(),
                    p.getBio(),
                    p.getAvatar()
                ));
            })
            .orElse(ResponseEntity.notFound().build());
    }
   
}
