package dev.yol.final_project_back.implementations;

import org.springframework.stereotype.Service;

@Service
public interface IRegisterService <T,S>{
    S register(T request);
}
