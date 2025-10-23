package dev.yol.final_project_back.trip;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dev.yol.final_project_back.implementations.IService;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;

public interface ITripService extends IService<TripResponseDTO, TripRequestDTO>{
    List<TripResponseDTO> getEntities();
    TripResponseDTO createEntity(TripRequestDTO tripRequestDTO, List<MultipartFile> files);
    TripResponseDTO getById(Long id);
    TripResponseDTO updateEntity(Long id, TripRequestDTO tripRequestDTO);
    void deleteEntity(Long id);
}
