package psi_hms.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import psi_hms.common.GenericException;
import psi_hms.common.GenericResponse;
import psi_hms.common.NotFoundException;
import psi_hms.dto.DoctorDTO;
import psi_hms.entity.Doctor;
import psi_hms.entity.Specialization;
import psi_hms.interfaces.DoctorInterface;
import psi_hms.repository.DoctorRepository;
import psi_hms.repository.SpecializationRepository;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorInterface {

    Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private MessageSource messageSource;

    private final String DOCTOR_SUBMIT_SUCCESS="doctor.submit.success";

    @Transactional
    @Override
    public GenericResponse createDoctor(DoctorDTO doctorDTO) {
        logger.info("API call to create a doctor{}", doctorDTO);

        String email = doctorDTO.getEmail();
        String phoneNumber = doctorDTO.getPhoneNumber();
        Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);
        Optional<Doctor> phNumber = doctorRepository.findByPhoneNumber(phoneNumber);
        Optional<Specialization> specialisationName = doctorRepository.findBySpecializationName(doctorDTO.getSpecialization().getSpecializationName());
        if (doctorOptional.isPresent()) {
           throw new GenericException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), messageSource.getMessage("error.message.doctor.already.exist", null, Locale.ENGLISH));
       } else if (phNumber.isPresent()) {
            throw new GenericException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), messageSource.getMessage("error.message.phNumber", null, Locale.ENGLISH));
              throw new GenericException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), messageSource.getMessage("error.message.phNumber", null, Locale.ENGLISH));}
        }else {
            Doctor doctor = new Doctor();
            doctor.setFirstName(doctorDTO.getFirstName());
            doctor.setLastName(doctorDTO.getLastName());
            doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
            doctor.setGender(doctorDTO.getGender());
            doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
            doctor.setEmail(doctorDTO.getEmail());

            Specialization specialization = new Specialization();
            specialization.setSpecializationName(doctorDTO.getSpecialization().getSpecializationName());
            doctor.setSpecialization(specialization);
            specializationRepository.save(specialization);

            doctorRepository.save(doctor);
        }
        GenericResponse response=new GenericResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setResponseCode(DOCTOR_SUBMIT_SUCCESS);
        response.setResponseMessage(messageSource.getMessage(DOCTOR_SUBMIT_SUCCESS, null, Locale.ENGLISH));

        return response;
    }

    @Transactional
    @Override
    public ResponseEntity<String> updateDoctor(Long id, DoctorDTO doctorDTO){

        logger.info("API call to update a doctor{}", doctorDTO);
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.message", null, Locale.ENGLISH)));

        existingDoctor.setFirstName(doctorDTO.getFirstName());
        existingDoctor.setLastName(doctorDTO.getLastName());
        existingDoctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        existingDoctor.setGender(doctorDTO.getGender());
        existingDoctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        existingDoctor.setEmail(doctorDTO.getEmail());

        Specialization specialization = existingDoctor.getSpecialization();
        specialization.setSpecializationName(doctorDTO.getSpecialization().getSpecializationName());
        specializationRepository.save(specialization);

        doctorRepository.save(existingDoctor);


        return ResponseEntity.ok("Doctor updated successfully");
    }

    @Override
    public List<Doctor> getAllDoctors() {

        logger.info("API call to get a list of doctors");
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(Long doctorId) {

        logger.info("API call to get a list of doctors by id doctorId: {}", doctorId);
        var doctor = doctorRepository.findById(doctorId);
        if(doctor.isEmpty()){
            throw new NotFoundException(messageSource.getMessage("error.message", null, Locale.ENGLISH));
        }
        return doctor;
    }
}