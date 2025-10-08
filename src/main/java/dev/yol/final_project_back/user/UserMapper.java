package dev.yol.final_project_back.user;

import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import dev.yol.final_project_back.user.dtos.UserRequestDTO;
import dev.yol.final_project_back.user.dtos.UserResponseDTO;

public class UserMapper {
    public UserEntity userRequestDtoToUserEntity(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRequestDTO.email());
        userEntity.setPassword(userRequestDTO.password());

        ProfileEntity profile = new ProfileEntity();
        profile.setDni(userRequestDTO.dni());
        profile.setName(userRequestDTO.name());
        profile.setFirstSurname(userRequestDTO.firstSurname());
        profile.setSecondSurname(userRequestDTO.secondSurname());
        profile.setPhoneNumber(userRequestDTO.phoneNumber());
        profile.setUser(userEntity);
        userEntity.setProfile(profile);

        Set<RoleEntity> roles = userRequestDTO.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException(roleName)))
                .collect(Collectors.toSet());
        userEntity.setRoles(roles);

        return userEntity;
    }

    public UserResponseDTO userEntityToUserResponseDto(UserEntity userEntity) {
        Set<String> roles = userEntity.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                userEntity.getId_user(),
                userEntity.getEmail(),
                userEntity.getProfile().getDni(),
                userEntity.getProfile().getName(),
                userEntity.getProfile().getFirstSurname(),
                userEntity.getProfile().getSecondSurname(),
                userEntity.getProfile().getPhoneNumber(),
                roles
        );
    }
}
