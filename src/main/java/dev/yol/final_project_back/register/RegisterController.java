package dev.yol.final_project_back.register;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yol.final_project_back.implementations.IRegisterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api-endpoint}/register")
@RequiredArgsConstructor
public class RegisterController {
    
     private final IRegisterService<RegisterRequestDTO, RegisterResponseDTO> registerService;

    @PostMapping("")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerService.register(dto));
    }
}
