package psi_hms.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import psi_hms.common.GenericResponse;
import psi_hms.dto.DoctorDTO;
import psi_hms.entity.Doctor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorInterface {

    /**
     * Adds a new doctor along with specialization details using a POST API.
     *
     * @param doctorDTO The DoctorDTO object containing the details of the doctor to be created.
     * @return A GenericResponse indicating the status of the operation.
     * @throws psi_hms.common.GenericException if the doctorDTO contains invalid data.
     */
    public GenericResponse createDoctor(DoctorDTO doctorDTO);

    /**
     * Updates the details of an existing doctor using a PUT API.
     *
     * @param id The ID of the doctor to be updated.
     * @param doctorDTO The updated DoctorDTO object containing the new details of the doctor.
     * @return A GenericResponse indicating the status of the operation.
     * @throws psi_hms.common.NotFoundException if the id is not found
     */
    public GenericResponse updateDoctor(Long id, DoctorDTO doctorDTO);

    /**
     * Retrieves a list of all doctors.
     *
     * @return A list of Doctors present in table
     */
    List<Doctor> getAllDoctors();

    /**
     * Retrieves details of a single doctor by their ID.
     *
     * @param doctorId The ID of the doctor to retrieve.
     * @return doctor
     * @throws psi_hms.common.NotFoundException if the doctorId is empty.
     */
    Optional<Doctor> getDoctorById(Long doctorId);

    /**
     *
     * @param pageNo
     * @param pageSize The ID will be divided as requested
     * @return list of doctors in defined page
     */

    List<Doctor> getDoctors(Integer pageNo, Integer pageSize);

    //List<Doctor> findByFilter(String firstName, String lastName, LocalDate dateOfBirth, String gender, String phoneNumber, String email);

    Page<Doctor> findByFilter(String firstName, String lastName, LocalDate dateOfBirth, String gender, String phoneNumber, String email, Pageable pageable);

    Page<Doctor> findByPageableFilter(String firstName, String lastName, String phoneNumber, String email, Pageable pageable);
}
