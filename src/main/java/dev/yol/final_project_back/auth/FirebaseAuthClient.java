package dev.yol.final_project_back.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import dev.yol.final_project_back.globals.InvalidLoginCredentialsException;

@Component
public class FirebaseAuthClient {
    private static final String API_KEY_PARAM = "key";
    private static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
    private static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";

    @Value("${com.tuapp.firebase.web-api-key}")
    private String webApiKey;

    public FirebaseSignInResponse login(String emailId, String password) {
        FirebaseSignInRequest requestBody = new FirebaseSignInRequest(emailId, password, true);
        return sendSignInRequest(requestBody);
    }

    private FirebaseSignInResponse sendSignInRequest(FirebaseSignInRequest firebaseSignInRequest) {
        try {
            return RestClient.create(SIGN_IN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder.queryParam(API_KEY_PARAM, webApiKey).build())
                    .body(firebaseSignInRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FirebaseSignInResponse.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getResponseBodyAsString().contains(INVALID_CREDENTIALS_ERROR)) {
                throw new InvalidLoginCredentialsException("Invalid login credentials provided");
            }
            throw exception;
        }
    }
}
