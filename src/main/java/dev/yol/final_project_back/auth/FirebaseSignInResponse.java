package dev.yol.final_project_back.auth;

public record FirebaseSignInResponse(
    String idToken,
    String refreshToken
) {

}
