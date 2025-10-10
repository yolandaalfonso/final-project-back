package dev.yol.final_project_back.user.dtos;

public record UserResponseDTO(
    Long id_user,
    String email,
    String name,
    String userName,
    String firstSurname,
    String bio,
    String avatar
) {

}
