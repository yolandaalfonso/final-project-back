package dev.yol.final_project_back.trip;

import java.util.Date;
import java.util.List;

import dev.yol.final_project_back.profile.ProfileEntity;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;

public class TripMapper {
    public static TripEntity toEntity(TripRequestDTO dtoRequest) {
        TripEntity trip = new TripEntity();
        trip.setTitle(dtoRequest.title());
        trip.setDescription(dtoRequest.description());
        trip.setCoverImage(dtoRequest.coverImage());
        trip.setCountry(dtoRequest.country());
        trip.setStartDate(dtoRequest.startDate());
        trip.setEndDate(dtoRequest.endDate());
        return trip;
    }

public static TripResponseDTO toDTO(TripEntity entity) {
        Long travelerId = null;
        String travelerFullName = null;

        if (entity.getTraveler() != null) {
            travelerId = entity.getTraveler().getId_user();

            // Accedemos al perfil para obtener el nombre completo
            if (entity.getTraveler().getProfile() != null) {
                ProfileEntity profile = entity.getTraveler().getProfile();
                travelerFullName = profile.getName() + " " + profile.getFirstSurname() + " " /* + profile.getSecondSurname() */;
            }
        }

        // Retornamos el DTO con todos los campos, incluyendo tutor y su nombre completo
        return new TripResponseDTO(
            entity.getId_trip(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getCoverImage(),
            entity.getCountry(),
            entity.getStartDate(),
            entity.getEndDate(),
            travelerId
        );
    }
}
