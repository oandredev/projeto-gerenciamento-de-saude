package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.dto.response.profile.ProfileItem;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByNameAndUser(String name, User user);

    List<ProfileItem> findByUser(User user);
}