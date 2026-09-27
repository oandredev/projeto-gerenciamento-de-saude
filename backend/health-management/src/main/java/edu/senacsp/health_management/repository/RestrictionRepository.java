package edu.senacsp.health_management.repository;

import edu.senacsp.health_management.entity.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
}
