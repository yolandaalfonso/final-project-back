package dev.yol.final_project_back.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.firebase.auth.FirebaseAuth;

import dev.yol.final_project_back.profile.ProfileEntity;

@WebMvcTest(controllers = UserController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
    }
)

public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository; // 🔹 Se simula, no se conecta a DB real

    @Test
    @DisplayName("GET /users/{id} debe devolver un usuario si existe")
    void testGetUserById_ReturnsUser() throws Exception {
        // Arrange
        ProfileEntity profile = new ProfileEntity();
        profile.setName("Juan");
        profile.setUserName("juanito");
        profile.setFirstSurname("Lopez");
        profile.setBio("Viajero del mundo");
        profile.setAvatar("avatar.jpg");

        UserEntity user = UserEntity.builder()
                .id_user(1L)
                .uid("firebase123")
                .email("user@test.com")
                .password("12345")
                .profile(profile)
                .build();

        profile.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act + Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.userName").value("juanito"))
                .andExpect(jsonPath("$.avatar").value("avatar.jpg"));
    }

    @Test
    @DisplayName("GET /users/{id} debe devolver 404 si no existe")
    void testGetUserById_NotFound() throws Exception {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/users/99"))
                .andExpect(status().isNotFound());
    }
}

