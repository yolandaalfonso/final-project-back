package dev.yol.final_project_back.user.dtos;

public record UserRequestDTO(
    String userName,
    String email,
    String password, 
    String name, 
    String firstSurname,
    String bio, 
    String avatar
) {

}
