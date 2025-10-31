package dev.yol.final_project_back.trip;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<TripEntity, Long>{
    /* List<TripEntity> findByTraveler_Id_user(Long idUser); */

    @Query("SELECT t FROM TripEntity t WHERE t.traveler.id_user = :userId")
    List<TripEntity> findByTravelerIdUser(@Param("userId") Long userId);


}
