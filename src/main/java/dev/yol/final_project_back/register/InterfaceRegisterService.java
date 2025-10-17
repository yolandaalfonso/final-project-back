package dev.yol.final_project_back.register;

import org.springframework.stereotype.Service;

import dev.yol.final_project_back.implementations.IRegisterService;

@Service
public interface InterfaceRegisterService extends IRegisterService<RegisterRequestDTO, RegisterResponseDTO>{

}
