package dev.yol.final_project_back.register;

import java.util.Base64;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserRepository;

public class RegisterServiceImpl implements InterfaceRegisterService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


        /* ProfileEntity profile = ProfileEntity.builder()
                .dni(dto.dni())
                .name(dto.name())
                .firstSurname(dto.firstSurname())
                .secondSurname(dto.secondSurname())
                .phoneNumber(dto.phoneNumber())
                .user(user)
                .build();

        user.setProfile(profile); */

        UserEntity saved = userRepository.save(user);

        return new RegisterResponseDTO(
                saved.getId_user(),
                saved.getEmail(),
                saved.getUserName()()
                /* saved.getProfile().getDni(),
                saved.getProfile().getName(),
                saved.getProfile().getFirstSurname(),
                saved.getProfile().getSecondSurname(),
                saved.getProfile().getPhoneNumber(),
                saved.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()) */
        );
    }
}
