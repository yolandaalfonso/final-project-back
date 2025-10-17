package dev.yol.final_project_back.profile;

import dev.yol.final_project_back.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfile;

    @Column(nullable = false, unique = true, length = 100)
    private String userName; 

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String firstSurname;

    @Column(length = 1500)
    private String bio;

    @Column(length = 500)
    @Builder.Default
    private String avatar = "/avatars/default-avatar.png"; // Temporal, luego Firebase

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
}
