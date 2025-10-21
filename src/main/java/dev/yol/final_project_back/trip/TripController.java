package dev.yol.final_project_back.trip;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.yol.final_project_back.implementations.IService;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;

@RestController
@RequestMapping(path="${api-endpoint}/trips")
public class TripController {
    
    private final IService<TripResponseDTO, TripRequestDTO> service;
    private final ITripService tripService;

    public TripController(IService<TripResponseDTO, TripRequestDTO> service, ITripService tripService) {
        this.service = service;
        this.tripService = tripService;
    }

    @GetMapping("")
    public List<TripResponseDTO> index() {
        return service.getEntities();
    }

     @PostMapping("")
    public ResponseEntity<TripResponseDTO> createEntity(@RequestBody TripRequestDTO dtoRequest) {

        if (dtoRequest.title().isBlank()) return ResponseEntity.badRequest().build();

        TripResponseDTO entityStored = service.createEntity(dtoRequest);

        if (entityStored == null) return ResponseEntity.noContent().build();

        return ResponseEntity.status(201).body(entityStored);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> show(@PathVariable("id") Long id) {
        TripResponseDTO trip = service.getById(id);
        return ResponseEntity.ok().body(trip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponseDTO> update(@PathVariable("id") Long id, @RequestBody TripRequestDTO dtoRequest) {

        
        TripResponseDTO updated = service.updateEntity(id, dtoRequest);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
       
        service.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    
}
