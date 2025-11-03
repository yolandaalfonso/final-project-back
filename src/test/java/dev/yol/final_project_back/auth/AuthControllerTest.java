package dev.yol.final_project_back.auth;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import dev.yol.final_project_back.globals.InvalidLoginCredentialsException;
import dev.yol.final_project_back.profile.ProfileEntity;
import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserMapper;
import dev.yol.final_project_back.user.UserRepository;
import dev.yol.final_project_back.user.dtos.UserResponseDTO;

public class AuthControllerTest {
    @Mock
    private FirebaseAuthClient firebaseAuthClient;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // standaloneSetup + placeholder for ${api-endpoint}
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .addPlaceholderValue("api-endpoint", "/api")
                .build();
    }

    // 🔹 Test: login exitoso (ajustado al record con idToken/refreshToken)
    @Test
    void login_shouldReturnOk_whenCredentialsAreValid() throws Exception {
        // tu record: FirebaseSignInResponse(String idToken, String refreshToken)
        FirebaseSignInResponse response = new FirebaseSignInResponse("id-token-123", "refresh-456");

        when(firebaseAuthClient.login("test@example.com", "1234")).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"1234\", \"returnSecureToken\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idToken").value("id-token-123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-456"));
    }

    // 🔹 Test: login fallido (credenciales inválidas)
    @Test
    void login_shouldReturnUnauthorized_whenCredentialsInvalid() throws Exception {
        when(firebaseAuthClient.login("bad@example.com", "wrong"))
                .thenThrow(new InvalidLoginCredentialsException());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"bad@example.com\", \"password\": \"wrong\", \"returnSecureToken\": true}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid login credentials"));
    }

    // 🔹 Test: getCurrentUser con principal válido
    @Test
    void getCurrentUser_shouldReturnUser_whenPrincipalIsValid() {
        Principal principal = () -> "user@example.com";

        UserEntity user = new UserEntity();
        ProfileEntity profile = new ProfileEntity();
        user.setEmail("user@example.com");
        user.setUid("uid123");

        // rellena profile y relación si tu model lo necesita
        profile.setName("John");
        profile.setUserName("jdoe");
        profile.setFirstSurname("Doe");
        profile.setBio("Hello world");
        profile.setAvatar("avatar.jpg");

        profile.setUser(user);
        user.setProfile(profile);

        // Asegúrate del orden de campos en tu record UserResponseDTO:
        // Long id_user, String email, String name, String uid, String userName, String firstSurname, String bio, String avatar
        UserResponseDTO dto = new UserResponseDTO(
            1L,
            "user@example.com",
            "John",
            "uid123",
            "jdoe",
            "Doe",
            "Hello world",
            "avatar.jpg"
        );

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userMapper.userEntityToUserResponseDto(user)).thenReturn(dto);

        var response = authController.getCurrentUser(principal);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().email()).isEqualTo("user@example.com");
        assertThat(response.getBody().userName()).isEqualTo("jdoe");
        assertThat(response.getBody().bio()).isEqualTo("Hello world");
    }

    // 🔹 Test: getCurrentUser sin principal
    @Test
    void getCurrentUser_shouldReturnUnauthorized_whenPrincipalIsNull() {
        var response = authController.getCurrentUser(null);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    // 🔹 Test: getCurrentUser cuando no se encuentra el usuario
    @Test
    void getCurrentUser_shouldThrow_whenUserNotFound() {
        Principal principal = () -> "missing@example.com";
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> authController.getCurrentUser(principal)
        );
    }

    // 🔹 Test: getUserByUid existente
    @Test
    void getUserByUid_shouldReturnOk_whenUserExists() throws Exception {
        UserEntity user = new UserEntity();
        ProfileEntity profile = new ProfileEntity();
        user.setUid("uid123");
        user.setEmail("user@example.com");
        profile.setName("John");
        profile.setUser(user);
        user.setProfile(profile);

        UserResponseDTO dto = new UserResponseDTO(
            1L,
            "user@example.com",
            "John",
            "uid123",
            "jdoe",
            "Doe",
            "Hello world",
            "avatar.jpg"
        );

        when(userRepository.findByUid("uid123")).thenReturn(Optional.of(user));
        when(userMapper.userEntityToUserResponseDto(user)).thenReturn(dto);

        mockMvc.perform(get("/api/auth/user/uid123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("uid123"))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    // 🔹 Test: getUserByUid no encontrado
    @Test
    void getUserByUid_shouldReturnNotFound_whenUserMissing() throws Exception {
        when(userRepository.findByUid("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/user/missing"))
                .andExpect(status().isNotFound());
    }

}
