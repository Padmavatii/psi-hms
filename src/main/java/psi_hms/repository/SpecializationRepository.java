package psi_hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psi_hms.entity.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Specialization findBySpecializationName(String specializationName);
}
