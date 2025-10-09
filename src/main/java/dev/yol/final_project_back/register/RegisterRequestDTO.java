package dev.yol.final_project_back.register;

public record RegisterRequestDTO(
        String email,
        String password,
        String dni,
        String userName
) {

}
