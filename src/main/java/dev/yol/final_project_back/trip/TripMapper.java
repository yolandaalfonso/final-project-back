package dev.yol.final_project_back.trip;

import java.util.List;
import java.util.stream.Collectors;

import dev.yol.final_project_back.images.ImageDTO;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;

public class TripMapper {
    public static TripEntity toEntity(TripRequestDTO dtoRequest) {
        TripEntity trip = new TripEntity();
        trip.setTitle(dtoRequest.title());
        trip.setDescription(dtoRequest.description());
        /* trip.setCoverImage(dtoRequest.coverImage()); */
        trip.setCountry(dtoRequest.country());
        trip.setStartDate(dtoRequest.startDate());
        trip.setEndDate(dtoRequest.endDate());
        return trip;
    }

 public static TripResponseDTO toDTO(TripEntity entity) {
        
    // 🧩 Convertir las imágenes asociadas al viaje en una lista de DTOs
        List<ImageDTO> imageDTOs = null;
        if (entity.getImages() != null) {
            imageDTOs = entity.getImages()
                .stream()
                .map(img -> new ImageDTO(
                    img.getId_image(),
                    img.getUrl()
                ))
                .collect(Collectors.toList());
        }

        // 🧍‍♀️ Datos del viajero
        Long travelerId = null;
        if (entity.getTraveler() != null) {
            travelerId = entity.getTraveler().getId_user();
        }

        // ✅ Crear el TripResponseDTO con todos los datos
        return new TripResponseDTO(
            entity.getId_trip(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getCountry(),
            entity.getStartDate(),
            entity.getEndDate(),
            imageDTOs,     // 👈 Aquí pasamos la lista de imágenes convertidas
            travelerId
        );
    }

        // Retornamos el DTO con todos los campos, incluyendo traveler y su nombre completo
        /* return new TripResponseDTO(
            entity.getId_trip(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getCountry(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getImages(),
            travelerId
        ); */
    } 

