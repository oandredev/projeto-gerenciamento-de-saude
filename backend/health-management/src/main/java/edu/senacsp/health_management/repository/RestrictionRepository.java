package edu.senacsp.health_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.senacsp.health_management.entity.Restriction;

import java.util.List;
import java.util.Optional;

public interface RestrictionRepository extends JpaRepository<Restriction, Long> {

    List<Restriction> findAllByProfileId(Long profileId);
    Optional<Restriction> findByIdAndProfileIdAndProfileUserId(Long id, Long profileId, Long userId);
}