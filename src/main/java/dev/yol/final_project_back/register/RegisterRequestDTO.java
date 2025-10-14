package dev.yol.final_project_back.register;

public record RegisterRequestDTO(
        String email,
        String password,
        String name,
        String firstSurname,
        String userName,
        String bio, 
        String avatar

) {

}
