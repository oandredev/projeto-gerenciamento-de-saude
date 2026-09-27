package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.dto.response.restriction.RestrictionItem;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
    // TODO Check return of RestrictionItem
    List<RestrictionItem> findAllByProfile(Profile profile);
    Optional<Restriction> findByIdAndProfile(Long id, Profile profile);
}