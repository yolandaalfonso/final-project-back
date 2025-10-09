package dev.yol.final_project_back.user.dtos;

public record UserRequestDTO(
    String userName,
    String email,
    String password
) {

}
