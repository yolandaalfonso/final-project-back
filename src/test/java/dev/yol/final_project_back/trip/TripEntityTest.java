package dev.yol.final_project_back.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.yol.final_project_back.images.ImageEntity;
import dev.yol.final_project_back.user.UserEntity;

public class TripEntityTest {
    @Test
    @DisplayName("Debe crear un viaje correctamente con sus atributos")
    void testCreateTrip() {
        // Arrange
        TripEntity trip = new TripEntity();
        Date start = new Date();
        Date end = new Date();

        // Act
        trip.setId_trip(1L);
        trip.setTitle("Viaje a Japón");
        trip.setDescription("Un viaje increíble por Tokio y Kioto");
        trip.setCountry("Japón");
        trip.setStartDate(start);
        trip.setEndDate(end);

        // Assert
        assertEquals(1L, trip.getId_trip());
        assertEquals("Viaje a Japón", trip.getTitle());
        assertEquals("Un viaje increíble por Tokio y Kioto", trip.getDescription());
        assertEquals("Japón", trip.getCountry());
        assertEquals(start, trip.getStartDate());
        assertEquals(end, trip.getEndDate());
    }

    @Test
    @DisplayName("Debe permitir asignar un viajero correctamente")
    void testTravelerAssignment() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .id_user(1L)
                .email("viajero@test.com")
                .uid("firebase123")
                .build();

        TripEntity trip = new TripEntity();
        trip.setTraveler(user);

        // Assert
        assertNotNull(trip.getTraveler());
        assertEquals("viajero@test.com", trip.getTraveler().getEmail());
        assertEquals("firebase123", trip.getTraveler().getUid());
    }

    @Test
    @DisplayName("Debe permitir agregar imágenes y mantener la relación bidireccional")
    void testAddImages() {
        // Arrange
        TripEntity trip = new TripEntity();
        trip.setImages(new HashSet<>());

        ImageEntity image1 = new ImageEntity();
        image1.setId_image(1L);
        image1.setUrl("foto1.jpg");
        image1.setTrip(trip);

        ImageEntity image2 = new ImageEntity();
        image2.setId_image(2L);
        image2.setUrl("foto2.jpg");
        image2.setTrip(trip);

        // Act
        trip.getImages().add(image1);
        trip.getImages().add(image2);

        // Assert
        assertEquals(2, trip.getImages().size());
        assertTrue(trip.getImages().contains(image1));
        assertEquals(trip, image1.getTrip());
    }

    @Test
    @DisplayName("Debe permitir eliminar imágenes del viaje correctamente")
    void testRemoveImages() {
        TripEntity trip = new TripEntity();
        trip.setImages(new HashSet<>());

        ImageEntity image = new ImageEntity();
        image.setId_image(1L);
        image.setUrl("foto.jpg");
        image.setTrip(trip);

        trip.getImages().add(image);
        assertEquals(1, trip.getImages().size());

        // Act
        trip.getImages().remove(image);

        // Assert
        assertEquals(0, trip.getImages().size());
    }
}
