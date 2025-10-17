package dev.yol.final_project_back.register;

public record RegisterResponseDTO(
    Long id_user,
    String email,
    String userName,
    String name, 
    String firstSurname,
    String bio, 
    String avatar
) {

}
