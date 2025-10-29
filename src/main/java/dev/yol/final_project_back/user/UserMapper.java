package dev.yol.final_project_back.user;

import dev.yol.final_project_back.profile.ProfileEntity;
import dev.yol.final_project_back.user.dtos.UserRequestDTO;
import dev.yol.final_project_back.user.dtos.UserResponseDTO;

public class UserMapper {
    public UserEntity userRequestDtoToUserEntity(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userRequestDTO.email());
        userEntity.setPassword(userRequestDTO.password());

        ProfileEntity profile = new ProfileEntity();
        profile.setUserName(userRequestDTO.userName());
        profile.setName(userRequestDTO.name());
        profile.setFirstSurname(userRequestDTO.firstSurname());
        profile.setBio(userRequestDTO.bio());
        profile.setAvatar(userRequestDTO.avatar());
        profile.setUser(userEntity);
        userEntity.setProfile(profile);

       /*  Set<RoleEntity> roles = userRequestDTO.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException(roleName)))
                .collect(Collectors.toSet());
        userEntity.setRoles(roles); */

        return userEntity;
    }

    public UserResponseDTO userEntityToUserResponseDto(UserEntity userEntity) {
        /* Set<String> roles = userEntity.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet()); */

        return new UserResponseDTO(
                userEntity.getId_user(),
                userEntity.getEmail(),
                userEntity.getProfile().getName(),
                userEntity.getUid(),
                userEntity.getProfile().getUserName(),
                userEntity.getProfile().getFirstSurname(),
                userEntity.getProfile().getBio(),
                userEntity.getProfile().getAvatar()
                /* roles */
        );
    }
}
