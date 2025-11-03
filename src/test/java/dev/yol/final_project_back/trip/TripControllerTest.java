package dev.yol.final_project_back.trip;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import dev.yol.final_project_back.images.ImageDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;

import dev.yol.final_project_back.implementations.IService;
import dev.yol.final_project_back.security.TokenAuthenticationFilter;
import dev.yol.final_project_back.trip.dtos.TripRequestDTO;
import dev.yol.final_project_back.trip.dtos.TripResponseDTO;

@WebMvcTest(TripController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IService<TripResponseDTO, TripRequestDTO> service;

    @MockBean
    private ITripService tripService;

    @MockBean
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @MockBean
    private FirebaseAuth firebaseAuth;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTrips() throws Exception {
        TripResponseDTO trip = new TripResponseDTO(
                1L,
                "Aventura en Islandia",
                "Explorando glaciares y cascadas",
                "Islandia",
                new Date(),
                new Date(System.currentTimeMillis() + 86400000L),
                List.of(new ImageDTO(10L, "islandia1.jpg"), new ImageDTO(, 12L, "islandia2.jpg")),
                "juanito"
        );

        when(service.getEntities()).thenReturn(List.of(trip));

        mockMvc.perform(get("/api/v1/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Aventura en Islandia"))
                .andExpect(jsonPath("$[0].country").value("Islandia"))
                .andExpect(jsonPath("$[0].travelerUsername").value("juanito"));
    }

    @Test
    void testGetTripById() throws Exception {
        TripResponseDTO trip = new TripResponseDTO(
                1L,
                "Ruta por Italia",
                "Visitando Roma y Florencia",
                "Italia",
                new Date(),
                new Date(System.currentTimeMillis() + 172800000L),
                List.of(new ImageDTO(8L, "roma.jpg")),
                "maria"
        );

        when(service.getById(1L)).thenReturn(trip);

        mockMvc.perform(get("/api/v1/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Ruta por Italia"))
                .andExpect(jsonPath("$.travelerUsername").value("maria"));
    }

    @Test
    void testGetTripsByUser() throws Exception {
        TripResponseDTO trip1 = new TripResponseDTO(
                1L,
                "Aventura en Islandia",
                "Explorando glaciares y cascadas",
                "Islandia",
                new Date(),
                new Date(System.currentTimeMillis() + 86400000L),
                List.of(new ImageDTO(5L, "islandia1.jpg")),
                "juanito"
        );

        when(tripService.getTripsByUserId(1L)).thenReturn(List.of(trip1));

        mockMvc.perform(get("/api/v1/trips/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Aventura en Islandia"))
                .andExpect(jsonPath("$[0].travelerUsername").value("juanito"));
    }

    @Test
    void testGetTripsByUserEmpty() throws Exception {
        when(tripService.getTripsByUserId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/trips/user/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateTrip() throws Exception {
        TripRequestDTO request = new TripRequestDTO(
                "Viaje a Japón",
                "Conociendo Tokio y Kioto",
                "Japón",
                new Date(),
                new Date(System.currentTimeMillis() + 259200000L),
                List.of(new ImageDTO(3L, "tokio.jpg"))
        );

        TripResponseDTO response = new TripResponseDTO(
                1L,
                "Viaje a Japón",
                "Conociendo Tokio y Kioto",
                "Japón",
                request.startDate(),
                request.endDate(),
                request.images(),
                "akira"
        );

        when(tripService.createEntity(any(TripRequestDTO.class), any())).thenReturn(response);

        MockMultipartFile tripPart = new MockMultipartFile(
                "trip",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "images",
                "tokio.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/trips")
                        .file(tripPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_trip").value(1L))
                .andExpect(jsonPath("$.country").value("Japón"))
                .andExpect(jsonPath("$.travelerUsername").value("akira"));
    }

    @Test
    void testUpdateTrip() throws Exception {
        TripRequestDTO request = new TripRequestDTO(
                "Ruta Mediterránea",
                "Explorando Grecia y Croacia",
                "Grecia",
                new Date(),
                new Date(System.currentTimeMillis() + 345600000L),
                List.of(new ImageDTO(4L, "atenas.jpg"))
        );

        TripResponseDTO updated = new TripResponseDTO(
                1L,
                "Ruta Mediterránea",
                "Explorando Grecia y Croacia",
                "Grecia",
                request.startDate(),
                request.endDate(),
                request.images(),
                "sofia"
        );

        when(tripService.updateEntity(eq(1L), any(TripRequestDTO.class), any())).thenReturn(updated);

        MockMultipartFile tripPart = new MockMultipartFile(
                "trip",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(request).getBytes()
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "images",
                "atenas.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/trips/1")
                        .file(tripPart)
                        .file(imagePart)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Ruta Mediterránea"))
                .andExpect(jsonPath("$.travelerUsername").value("sofia"));
    }

    @Test
    void testDeleteTrip() throws Exception {
        doNothing().when(service).deleteEntity(1L);

        mockMvc.perform(delete("/api/v1/trips/1"))
                .andExpect(status().isNoContent());
    }
}
