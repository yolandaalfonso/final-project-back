package dev.yol.final_project_back.trip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.yol.final_project_back.firebase.FirebaseStorageService;
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
    private final FirebaseStorageService firebaseStorageService;

    public TripServiceImpl(TripRepository repository, UserRepository userRepository, FirebaseStorageService firebaseStorageService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.firebaseStorageService = firebaseStorageService;
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

    System.out.println("🔹 UID recibido del token en createEntity: " + uid);
    
    // 2️⃣ Buscar el usuario en tu base de datos por UID
    UserEntity user = userRepository.findByUid(uid)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con UID: " + uid));
    
    // 3️⃣ Crear y guardar el viaje
    TripEntity trip = TripMapper.toEntity(tripRequestDTO);
    trip.setTraveler(user);

    /* // 4️⃣ Si el DTO tiene imágenes, mapéalas y asígnalas al viaje
    if (tripRequestDTO.images() != null && !tripRequestDTO.images().isEmpty()) {
        tripRequestDTO.images().forEach(imgDto -> {
            ImageEntity image = new ImageEntity();
            image.setUrl(imgDto.url());
            image.setTrip(trip); // clave: establecer la relación inversa
            trip.getImages().add(image);
        });
    } */

    // 🔹 Subir imágenes a Firebase y asignarlas al viaje
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                try {
                    String imageUrl = firebaseStorageService.uploadFile(file);
                    ImageEntity image = new ImageEntity();
                    image.setUrl(imageUrl);
                    image.setTrip(trip);
                    trip.getImages().add(image);
                } catch (IOException e) {
                    throw new RuntimeException("Error al subir la imagen a Firebase", e);
                }
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

    /* @Override
    public TripResponseDTO updateEntity(Long id, TripRequestDTO tripRequestDTO) {
       /if (tripRequestDTO.traveler() == null) {
            /throw new TripException("User ID cannot be null");
        }
        TripEntity trip = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));

        UserEntity user = userRepository.findById(tripRequestDTO.traveler())
                .orElseThrow(() -> new UserNotFoundException("Viaje no encontrado con id: " + tripRequestDTO.traveler())); 

        // Actualizar los campos necesarios
        trip.setTitle(tripRequestDTO.title());
        trip.setDescription(tripRequestDTO.description());
       trip.setCoverImage(tripRequestDTO.coverImage());
        trip.setCountry(tripRequestDTO.country());
        trip.setStartDate(tripRequestDTO.startDate());
        trip.setEndDate(tripRequestDTO.endDate());
        trip.setTraveler(user); 

        TripEntity updatedEntity = repository.save(trip);
        return TripMapper.toDTO(updatedEntity);

    } */

    @Override
    public TripResponseDTO updateEntity(Long id, TripRequestDTO dtoRequest) {
        // implementación “básica”, por ejemplo sin manejar imágenes
        TripEntity trip = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setTitle(dtoRequest.title());
        trip.setDescription(dtoRequest.description());
        trip.setStartDate(dtoRequest.startDate());
        trip.setEndDate(dtoRequest.endDate());
        trip.setCountry(dtoRequest.country());

        TripEntity updated = repository.save(trip);
        return TripMapper.toDTO(updated);

        /* return new TripResponseDTO(
            updated.getId_trip(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getCountry(),
            updated.getStartDate(),
            updated.getEndDate(),
            updated.getImages(),
            updated.getTraveler() != null ? updated.getTraveler().getId_user() : null
        ); */
    }

    @Override
    public TripResponseDTO updateEntity(Long id, TripRequestDTO dtoRequest, List<MultipartFile> files) {
        TripEntity trip = repository.findById(id)
    .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));

        trip.setTitle(dtoRequest.title());
        trip.setDescription(dtoRequest.description());
        trip.setStartDate(dtoRequest.startDate());
        trip.setEndDate(dtoRequest.endDate());
        trip.setCountry(dtoRequest.country());

        if (files != null && !files.isEmpty()) {
            trip.getImages().clear();
            for (MultipartFile file : files) {
                try {
                    String imageUrl = firebaseStorageService.uploadFile(file);
                    ImageEntity image = new ImageEntity();
                    image.setUrl(imageUrl);
                    image.setTrip(trip);
                    trip.getImages().add(image);
                } catch (IOException e) {
                    throw new RuntimeException("Error al subir la imagen a Firebase", e);
                }
            }
        }

        TripEntity updated = repository.save(trip);
        return TripMapper.toDTO(updated);

    }



    /* @Override
    public TripResponseDTO updateEntity(Long id, TripRequestDTO tripRequestDTO, List<MultipartFile> files) {
    // 1️⃣ Buscar el viaje existente
    TripEntity trip = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));

    // 2️⃣ Actualizar los campos básicos
    trip.setTitle(tripRequestDTO.title());
    trip.setDescription(tripRequestDTO.description());
    trip.setCountry(tripRequestDTO.country());
    trip.setStartDate(tripRequestDTO.startDate());
    trip.setEndDate(tripRequestDTO.endDate());

    // 3️⃣ Manejar imágenes
    if (files != null && !files.isEmpty()) {
        // Si quieres reemplazar las imágenes anteriores:
        trip.getImages().clear();

        for (MultipartFile file : files) {
            // ⚠️ Aquí subirías la imagen a tu servicio de almacenamiento (Firebase Storage o local)
            // y obtendrías su URL pública
            String imageUrl = uploadImage(file);

            ImageEntity image = new ImageEntity();
            image.setUrl(imageUrl);
            image.setTrip(trip);
            trip.getImages().add(image);
        }
    }

    // 4️⃣ Guardar cambios
    TripEntity updatedEntity = repository.save(trip);

    // 5️⃣ Devolver el DTO actualizado
    return TripMapper.toDTO(updatedEntity); 
    } */


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
