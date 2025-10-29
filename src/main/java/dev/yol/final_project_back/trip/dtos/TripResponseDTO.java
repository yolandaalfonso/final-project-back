package dev.yol.final_project_back.trip.dtos;

import java.util.Date;
import java.util.List;

import dev.yol.final_project_back.images.ImageDTO;

public record TripResponseDTO(
    Long id_trip,
    String title, 
    String description, 
   /*  String coverImage, */
    String country,
    Date startDate,
    Date endDate,
    List<ImageDTO> images,
    Long traveler
) {

}
