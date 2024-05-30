package psi_hms.interfaces;

import org.springframework.http.ResponseEntity;
import psi_hms.dto.DoctorDTO;

public interface Doctor {

    public ResponseEntity<String> createDoctor(DoctorDTO doctorDTO);

    public ResponseEntity<String> updateDoctor(Long id, DoctorDTO doctorDTO);

}
