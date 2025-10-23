package dev.yol.final_project_back.trip;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.yol.final_project_back.images.ImageEntity;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;
import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserRepository;
import dev.yol.final_project_back.user.exceptions.UserNotFoundException;

@Service("tripService")
public class TripServiceImpl implements ITripService{
    
    private final TripRepository repository;
    private final UserRepository userRepository;

    public TripServiceImpl(TripRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TripResponseDTO> getEntities() {
        List<TripResponseDTO> trips = new ArrayList<>();

        repository.findAll().forEach(c -> {
            TripResponseDTO trip = TripMapper.toDTO(c);
            trips.add(trip);
        });

        return trips;
    }

    @Override
    public TripResponseDTO createEntity(TripRequestDTO tripRequestDTO) {
    // si no se mandan imágenes, simplemente crea el viaje normal
    return createEntity(tripRequestDTO, List.of());
    }


    @Override
    public TripResponseDTO createEntity(TripRequestDTO tripRequestDTO, List<MultipartFile> files) {
    // 1️⃣ Obtener el usuario autenticado del contexto de Spring Security
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String uid = authentication.getName(); // Firebase UID
    
    // 2️⃣ Buscar el usuario en tu base de datos por UID
    UserEntity user = userRepository.findByUid(uid)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con UID: " + uid));
    
    // 3️⃣ Crear y guardar el viaje
    TripEntity trip = TripMapper.toEntity(tripRequestDTO);
    trip.setTraveler(user);

    // 4️⃣ Si el DTO tiene imágenes, mapéalas y asígnalas al viaje
    if (tripRequestDTO.images() != null && !tripRequestDTO.images().isEmpty()) {
        tripRequestDTO.images().forEach(imgDto -> {
            ImageEntity image = new ImageEntity();
            image.setUrl(imgDto.url());
            image.setTrip(trip); // clave: establecer la relación inversa
            trip.getImages().add(image);
        });
    }

    TripEntity tripStored = repository.save(trip);

    // 4️⃣ Devolver el DTO
    return TripMapper.toDTO(tripStored);
    }
    
   /*  public TripResponseDTO createEntity(TripRequestDTO tripRequestDTO) {
        if (tripRequestDTO.traveler() == null) {
             throw new TripException("User ID cannot be null"); 
        }
        UserEntity user = userRepository.findById(tripRequestDTO.traveler())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + tripRequestDTO.traveler()));
        TripEntity trip = TripMapper.toEntity(tripRequestDTO);
        trip.setTraveler(user);
        TripEntity tripStored = repository.save(trip);
        return TripMapper.toDTO(tripStored) ;
    } */

    @Override
    public TripResponseDTO getById(Long id) {
        TripEntity trip = repository.findById(id).orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));
        return TripMapper.toDTO(trip);
    }

    @Override
    public TripResponseDTO updateEntity(Long id, TripRequestDTO tripRequestDTO) {
       /*  if (tripRequestDTO.traveler() == null) {
            /throw new TripException("User ID cannot be null");
        }*/
        TripEntity trip = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));

       /*  UserEntity user = userRepository.findById(tripRequestDTO.traveler())
                .orElseThrow(() -> new UserNotFoundException("Viaje no encontrado con id: " + tripRequestDTO.traveler())); */

        // Actualizar los campos necesarios
        trip.setTitle(tripRequestDTO.title());
        trip.setDescription(tripRequestDTO.description());
       /*  trip.setCoverImage(tripRequestDTO.coverImage()); */
        trip.setCountry(tripRequestDTO.country());
        trip.setStartDate(tripRequestDTO.startDate());
        trip.setEndDate(tripRequestDTO.endDate());
        /* trip.setTraveler(user); */

        TripEntity updatedEntity = repository.save(trip);
        return TripMapper.toDTO(updatedEntity);

    }

    @Override
    public void deleteEntity(Long id) {
        TripEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));
        repository.delete(entity);
    }

    

    /* @Override
    public TripResponseDTO getByIdentificationNumber(String identificationNumber) {
        PatientEntity patient = repository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new PatientException("No se encontró paciente con identificación: " + identificationNumber));
        return PatientMapper.toDTO(patient);
    }

    @Override
    public List<PatientResponseDTO> searchByName(String name) {
        List<PatientEntity> patients = repository.findByName(name);
        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    } */
}
