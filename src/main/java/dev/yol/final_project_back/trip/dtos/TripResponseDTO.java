package dev.yol.final_project_back.trip.dtos;

import java.util.Date;
import java.util.List;

public record TripResponseDTO(
    Long id_trip,
    String title, 
    String description, 
    String coverPhoto,
    List<String> photos, 
    String country,
    Date startDate,
    Date endDate 

) {

}
