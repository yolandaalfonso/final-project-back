package dev.yol.final_project_back.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByUid(String uid);
    Optional<UserEntity> findByEmail(String email);
}
