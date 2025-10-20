package dev.yol.final_project_back.trip;

import java.util.Date;
import java.util.List;

import dev.yol.final_project_back.user.UserEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
    private String coverPhoto;

    @ElementCollection
    @CollectionTable(
        name="trip_photos",
        joinColumns = @JoinColumn(name = "trip_id")
    )
    @Column(name = "photo_url")  
    private List<String> photos;

    private String country;
    private Date startDate;
    private Date endDate;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    /* @JsonBackReference */
    private UserEntity traveler;

    
}
