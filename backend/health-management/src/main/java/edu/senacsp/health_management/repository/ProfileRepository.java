package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.dto.response.profile.ProfileItem;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByNameAndUser(String name, User user);

    Optional<Profile> findByIdAndUserId(Long id, Long userId);

    List<ProfileItem> findAllByUser(User user);
}