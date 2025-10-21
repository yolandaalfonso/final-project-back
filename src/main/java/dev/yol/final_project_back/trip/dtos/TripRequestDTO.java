package dev.yol.final_project_back.trip.dtos;

import java.util.Date;
import java.util.List;

public record TripRequestDTO(
    String title, 
    String description, 
    String coverImage,
    String country,
    Date startDate,
    Date endDate, 
    Long traveler
) {

}
