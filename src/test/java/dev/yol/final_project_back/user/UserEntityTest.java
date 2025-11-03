package dev.yol.final_project_back.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.yol.final_project_back.profile.ProfileEntity;
import dev.yol.final_project_back.trip.TripEntity;

public class UserEntityTest {
    @Test
    @DisplayName("Debe crear un usuario correctamente con el builder")
    void testCreateUserWithBuilder() {
        UserEntity user = UserEntity.builder()
                .id_user(1L)
                .uid("firebase123")
                .email("user@test.com")
                .password("12345")
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId_user());
        assertEquals("firebase123", user.getUid());
        assertEquals("user@test.com", user.getEmail());
        assertEquals("12345", user.getPassword());
        assertNotNull(user.getTrips());
        assertTrue(user.getTrips().isEmpty());
    }

    @Test
    @DisplayName("Debe permitir asignar un perfil correctamente")
    void testProfileAssignment() {
        UserEntity user = new UserEntity();
        ProfileEntity profile = new ProfileEntity();
        profile.setBio("Amante de los viajes");
        profile.setUser(user);

        user.setProfile(profile);

        assertEquals(profile, user.getProfile());
        assertEquals(user, profile.getUser());
    }

    @Test
    @DisplayName("Debe permitir agregar y eliminar viajes correctamente")
    void testTripsAssociation() {
        UserEntity user = new UserEntity();
        TripEntity trip1 = new TripEntity();
        TripEntity trip2 = new TripEntity();

        trip1.setTraveler(user);
        trip2.setTraveler(user);

        user.setTrips(new HashSet<>());
        user.getTrips().add(trip1);
        user.getTrips().add(trip2);

        assertEquals(2, user.getTrips().size());
        assertTrue(user.getTrips().contains(trip1));
        assertTrue(user.getTrips().contains(trip2));

        user.getTrips().remove(trip1);
        assertEquals(1, user.getTrips().size());
    }
}
