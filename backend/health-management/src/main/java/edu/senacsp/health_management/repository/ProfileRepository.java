package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByNameAndUser(String name, User user);
    boolean existsByNameAndUserAndIdNot(String name, User user, Long id);

    Optional<Profile> findByIdAndUser(Long id, User user);

    List<Profile> findAllByUser(User user);
}