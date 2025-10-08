package dev.yol.final_project_back.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id_user;

    @Column(nullable = false, unique = true, length = 100)
    private String username; 
    @Column(nullable = false, unique = true, length = 100)
    private String email; 
    @Column(nullable = false)
    private String password;

    //Relación con Profile
    /* @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProfileEntity profile;

    //Relación con Pacientes, que nos permite eliminarlos en cascada cuando borramos el user correspondiente,    
    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<PatientEntity> patients = new HashSet<>(); */


    
}
