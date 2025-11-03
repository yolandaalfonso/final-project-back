package dev.yol.final_project_back.trip;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import dev.yol.final_project_back.firebase.FirebaseStorageService;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;
import dev.yol.final_project_back.user.UserEntity;
import dev.yol.final_project_back.user.UserRepository;
import dev.yol.final_project_back.user.exceptions.UserNotFoundException;

public class TripServiceImplTest {
    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FirebaseStorageService firebaseStorageService;

    @InjectMocks
    private TripServiceImpl tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext context = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("uid-123");
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("Debe devolver una lista de viajes correctamente")
    void testGetEntities() {
        TripEntity trip1 = new TripEntity();
        trip1.setId_trip(1L);
        TripEntity trip2 = new TripEntity();
        trip2.setId_trip(2L);
        when(tripRepository.findAll()).thenReturn(List.of(trip1, trip2));

        List<TripResponseDTO> result = tripService.getEntities();

        assertEquals(2, result.size());
        verify(tripRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe crear un viaje correctamente sin imágenes")
    void testCreateEntityWithoutImages() {
        TripRequestDTO dto = new TripRequestDTO("Viaje", "Desc", "España", new Date(), new Date(), null);
        UserEntity user = new UserEntity();
        user.setUid("uid-123");

        when(userRepository.findByUid("uid-123")).thenReturn(Optional.of(user));
        when(tripRepository.save(any(TripEntity.class))).thenAnswer(inv -> {
            TripEntity saved = inv.getArgument(0);
            saved.setId_trip(10L);
            return saved;
        });

        TripResponseDTO response = tripService.createEntity(dto);

        assertNotNull(response);
        assertEquals("Viaje", response.title());
        verify(userRepository).findByUid("uid-123");
        verify(tripRepository).save(any(TripEntity.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe al crear viaje")
    void testCreateEntityUserNotFound() {
        TripRequestDTO dto = new TripRequestDTO("Viaje", "Desc", "España", new Date(), new Date(), null);
        when(userRepository.findByUid("uid-123")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> tripService.createEntity(dto));
    }

    @Test
    @DisplayName("Debe crear un viaje con imágenes correctamente")
    void testCreateEntityWithImages() throws IOException {
        TripRequestDTO dto = new TripRequestDTO("Viaje", "Desc", "Italia", new Date(), new Date(), null);
        UserEntity user = new UserEntity();
        user.setUid("uid-123");
        when(userRepository.findByUid("uid-123")).thenReturn(Optional.of(user));

        MultipartFile mockFile = mock(MultipartFile.class);
        when(firebaseStorageService.uploadFile(mockFile)).thenReturn("https://firebase.com/foto.jpg");
        when(tripRepository.save(any(TripEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        TripResponseDTO response = tripService.createEntity(dto, List.of(mockFile));

        assertNotNull(response);
        verify(firebaseStorageService).uploadFile(mockFile);
        verify(tripRepository).save(any(TripEntity.class));
    }

    @Test
    @DisplayName("Debe devolver un viaje por su ID")
    void testGetById() {
        TripEntity trip = new TripEntity();
        trip.setId_trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        TripResponseDTO response = tripService.getById(1L);

        assertNotNull(response);
        verify(tripRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe devolver los viajes de un usuario por su ID")
    void testGetTripsByUserId() {
        TripEntity trip = new TripEntity();
        trip.setId_trip(1L);
        when(tripRepository.findByTravelerIdUser(5L)).thenReturn(List.of(trip));

        List<TripResponseDTO> response = tripService.getTripsByUserId(5L);

        assertEquals(1, response.size());
        verify(tripRepository).findByTravelerIdUser(5L);
    }

    @Test
    @DisplayName("Debe actualizar un viaje correctamente")
    void testUpdateEntity() {
        TripEntity trip = new TripEntity();
        trip.setId_trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(TripEntity.class))).thenReturn(trip);

        TripRequestDTO dto = new TripRequestDTO("Nuevo título", "Nueva desc", "Francia", new Date(), new Date(), null);
        TripResponseDTO response = tripService.updateEntity(1L, dto);

        assertEquals("Nuevo título", response.title());
        verify(tripRepository).save(any(TripEntity.class));
    }

    @Test
    @DisplayName("Debe eliminar un viaje correctamente")
    void testDeleteEntity() {
        TripEntity trip = new TripEntity();
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        tripService.deleteEntity(1L);

        verify(tripRepository).delete(trip);
    }
}
