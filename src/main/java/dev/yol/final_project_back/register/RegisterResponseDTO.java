package dev.yol.final_project_back.register;

public record RegisterResponseDTO(
    Long id_user,
    String email,
    String password,
    String userName
) {

}
