package psi_hms.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import psi_hms.common.GenericResponse;
import psi_hms.dto.DoctorDTO;
import psi_hms.entity.Doctor;
import psi_hms.interfaces.DoctorService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hms")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;


    @PostMapping("/doctor")
    public GenericResponse submitDoctor(@RequestBody @Valid DoctorDTO doctorDTO) {
       return doctorService.createDoctor(doctorDTO);
    }

    @PutMapping("/doctor/{id}")
    public GenericResponse updateDoctor(@PathVariable Long id, @RequestBody @Valid DoctorDTO doctorDTO) {
        return doctorService.updateDoctor(id, doctorDTO);
    }

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/doctor/{doctorId}")
    public Optional<Doctor> getDoctorById(@PathVariable Long doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @GetMapping("/pagination")
    public List<Doctor> getDoctorWithPaging(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "0") Integer pageSize) {
        return doctorService.getDoctors(pageNo, pageSize);
    }

    @GetMapping("/filter")
    public Page<Doctor> getDoctorWithfilter(@RequestParam(required = false) String firstName,
                                            @RequestParam(required = false) String lastName,
                                            @RequestParam(required = false) LocalDate dateOfBirth,
                                            @RequestParam(required = false) String gender,
                                            @RequestParam(required = false) String phoneNumber,
                                            @RequestParam(required = false) String email, Pageable pageable){
        return doctorService.findByFilter(firstName, lastName, dateOfBirth, gender,phoneNumber, email, pageable);
    }
}

