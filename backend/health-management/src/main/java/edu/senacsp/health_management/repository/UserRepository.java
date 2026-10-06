package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Fetches profiles together with the user (avoids LazyInitializationException).
    @EntityGraph(attributePaths = "profiles")
    Optional<User> findByEmail(String email);

    // Fetches profiles together with the user (avoids LazyInitializationException).
    @EntityGraph(attributePaths = "profiles")
    Optional<User> findWithProfilesById(Long id);

    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email, Long id);
}