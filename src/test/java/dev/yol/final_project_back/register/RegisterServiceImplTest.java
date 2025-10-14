package dev.yol.final_project_back.register;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.firebase.auth.FirebaseAuth;

import dev.yol.final_project_back.profile.ProfileEntity;
import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserRepository;

public class RegisterServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private  FirebaseAuth firebaseAuth;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private RegisterRequestDTO requestDTO;
    private UserEntity savedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos de entrada (Base64)
        String emailBase64 = Base64.getEncoder().encodeToString("user@example.com".getBytes());
        String passwordBase64 = Base64.getEncoder().encodeToString("plainPassword".getBytes());

        requestDTO = new RegisterRequestDTO(
                emailBase64,
                passwordBase64,
                "TestName",
                "TestFirstSurName",
                "TesUserName",
                "TestBio",
                ""
        );


        savedUser = UserEntity.builder()
                .id_user(1L)
                .email("user@example.com")
                .password("encodedPassword")
                .profile(ProfileEntity.builder()
                        .name("TestName")
                        .firstSurname("TestFirstSurname")
                        .userName("TestUserName")
                        .bio("TestBio")
                        .avatar("")
                        .build())
                .build();
    }

    @Test
    void testRegister_Success() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        RegisterResponseDTO response = registerService.register(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.id_user());
        assertEquals("user@example.com", response.email());
        assertEquals("TestName", response.name());
        assertEquals("TestFirstSurname", response.firstSurname());
        assertEquals("TestUserName", response.userName());
        assertEquals("TestBio", response.bio());
        assertEquals("", response.avatar());

        verify(userRepository).save(any(UserEntity.class));
    }

}
