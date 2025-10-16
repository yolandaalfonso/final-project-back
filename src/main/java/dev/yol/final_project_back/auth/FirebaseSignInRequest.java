package dev.yol.final_project_back.auth;

public record FirebaseSignInRequest(
    String email,
    String password,
    boolean returnSecureToken
) {
}
