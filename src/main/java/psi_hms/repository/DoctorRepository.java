package psi_hms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import psi_hms.entity.Doctor;

import java.time.LocalDate;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM doctor d " +
            "WHERE (:firstName IS NULL OR d.first_name = :firstName) " +
            "OR (:lastName IS NULL OR d.last_name = :lastName) " +
            "OR (:dateOfBirth IS NULL OR d.date_of_birth = :dateOfBirth) " +
            "OR (:gender IS NULL OR d.gender = :gender) " +
            "OR (:phoneNumber IS NULL OR d.phone_number = :phoneNumber) " +
            "OR (:email IS NULL OR d.email = :email) ", nativeQuery = true)
    Page<Doctor> findByFilters(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("dateOfBirth") LocalDate dateOfBirth,
                               @Param("gender") String gender, @Param("phoneNumber") String phoneNumber,@Param("email") String email, Pageable pageable);


    Page<Doctor> findByFirstNameOrLastNameOrPhoneNumberOrEmail(String firstName, String lastName,
                                                               String phoneNumber, String email, Pageable pageable);
}
