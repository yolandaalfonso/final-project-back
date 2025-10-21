package dev.yol.final_project_back.trip.dtos;

import java.util.Date;

public record TripResponseDTO(
    Long id_trip,
    String title, 
    String description, 
    String coverImage,
    String country,
    Date startDate,
    Date endDate, 
    Long traveler
) {

}
