package dev.yol.final_project_back.register;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.yol.final_project_back.implementations.IRegisterService;

public class RegisterControllerTest {
    @Mock
    private IRegisterService<RegisterRequestDTO, RegisterResponseDTO> registerService;

    @InjectMocks
    private RegisterController registerController;

    private RegisterRequestDTO requestDTO;
    private RegisterResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new RegisterRequestDTO(
                "dGVzdEBleGFtcGxlLmNvbQ==", 
                "cGFzc3dvcmQ=",            
                "Test",
                "Controller",
                "TestController",
                "Hi, I'm TestController",
                ""
        );

        responseDTO = new RegisterResponseDTO(
                1L,
                "test@example.com",
                "TestController",
                "Test",
                "Controller",
                "Hi, I'm TestController",
                ""
        );
    }

    @Test
    void testRegister_ReturnsCreatedResponse() {
        // Service devuelve un responseDTO
        when(registerService.register(requestDTO)).thenReturn(responseDTO);

        // Método del controller
        ResponseEntity<RegisterResponseDTO> response = registerController.register(requestDTO);

        // Verificar resultado
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@example.com", response.getBody().email());

        // Service invocado una sola vez
        verify(registerService, times(1)).register(requestDTO);
    }
}
