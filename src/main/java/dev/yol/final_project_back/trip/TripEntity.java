package dev.yol.final_project_back.trip;

import java.util.Date;

import dev.yol.final_project_back.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id_trip;

    private String title;
    private String description;
    private String coverImage;
    
    private String country;
    private Date startDate;
    private Date endDate;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    /* @JsonBackReference */
    private UserEntity traveler;


     /* //Relación con Imágenes, que nos permite eliminarlos en cascada cuando borramos el viaje correspondiente,    
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ImageEntity> images = new HashSet<>(); */

    
}
