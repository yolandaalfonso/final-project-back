package dev.yol.final_project_back.register;

import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import dev.yol.final_project_back.profile.ProfileEntity;
import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements InterfaceRegisterService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FirebaseAuth firebaseAuth;

    /* public RegisterServiceImpl(UserRepository userRepository, 
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    } */

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO dto) {

        //TODO: implement facades pattern

        byte[] decodedPasswordBytes = Base64.getDecoder().decode(dto.password());
        String decodedPassword = new String(decodedPasswordBytes);

        String hashedPassword = passwordEncoder.encode(decodedPassword);

    
        byte[] decodedEmailBytes = Base64.getDecoder().decode(dto.email());
        String decodedEmail = new String(decodedEmailBytes);


        /* RoleEntity defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name: ROLE_USER")); */

        UserEntity user = UserEntity.builder()
                .email(decodedEmail)   
                .password(hashedPassword)
                /* .roles(Set.of(defaultRole)) */
                .build();


        ProfileEntity profile = ProfileEntity.builder()
                .userName(dto.userName())
                .user(user)
                .build();

        user.setProfile(profile);

        // ✅ Crea el usuario también en Firebase Authentication
        try {
            firebaseAuth.createUser(new UserRecord.CreateRequest()
                    .setEmail(decodedEmail)
                    .setPassword(decodedPassword)
                    .setEmailVerified(true));
        } catch (FirebaseAuthException e) {
            if (e.getMessage().contains("EMAIL_EXISTS")) {
                throw new RuntimeException("El usuario ya existe en Firebase");
            }
            throw new RuntimeException("Error creando usuario en Firebase: " + e.getMessage());
        }


        UserEntity saved = userRepository.save(user);


        return new RegisterResponseDTO(
                saved.getId_user(),
                saved.getEmail(),
                saved.getProfile().getUserName(),
                saved.getProfile().getName(),
                saved.getProfile().getFirstSurname(),
                saved.getProfile().getBio(),
                saved.getProfile().getAvatar()
                /* saved.getProfile().getDni(),
                saved.getProfile().getName(),
                saved.getProfile().getFirstSurname(),
                saved.getProfile().getSecondSurname(),
                saved.getProfile().getPhoneNumber(),
                saved.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()) */
        );
    }
}
