package psi_hms.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import psi_hms.common.GenericException;
import psi_hms.common.GenericResponse;
import psi_hms.common.NotFoundException;
import psi_hms.dto.DoctorDTO;
import psi_hms.entity.Doctor;
import psi_hms.entity.Specialization;
import psi_hms.interfaces.DoctorService;
import psi_hms.repository.DoctorRepository;
import psi_hms.repository.SpecializationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;

    private final SpecializationRepository specializationRepository;

    private final MessageSource messageSource;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, SpecializationRepository specializationRepository, MessageSource messageSource) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.messageSource = messageSource;
    }

    private static final String DOCTOR_SUBMIT_SUCCESS="doctor.submit.success";
    private static final String DOCTOR_UPDATE_SUCCESS="doctor.update.success";
    private static final String DOCTOR_EMAIL_ERROR="doctor.email.exist.error";
    private static final String DOCTOR_PHONENUMBER_ERROR="doctor.phNumber.exist.error";
    private static final String DOCTOR_CREATE_FAIL="doctor.create.fail";
    private static final String DOCTOR_NOT_FOUND="doctor.details.not.found.error";


    @Transactional
    @Override
    public GenericResponse createDoctor(DoctorDTO doctorDTO) {

        try {
            logger.info("API call to create a doctor {}", doctorDTO);

            String email = doctorDTO.getEmail();
            String phoneNumber = doctorDTO.getPhoneNumber();
            Optional<Doctor> doctorOptional = doctorRepository.findByEmail(email);
            Optional<Doctor> phNumber = doctorRepository.findByPhoneNumber(phoneNumber);
            if (doctorOptional.isPresent()) {
                logger.error("Email already registered{}", email);
                throw new GenericException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), messageSource.getMessage(DOCTOR_EMAIL_ERROR, null, Locale.ENGLISH));
            } else if (phNumber.isPresent()) {
                logger.error("PhoneNumber already registered{}", phNumber);
                throw new GenericException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), messageSource.getMessage(DOCTOR_PHONENUMBER_ERROR, null, Locale.ENGLISH));
            } else {
                String specializationName = doctorDTO.getSpecialization().getSpecializationName();
                Specialization specialization = specializationRepository.findBySpecializationName(specializationName);
                if (specialization == null) {
                    logger.info("New specialization is created{}", specialization);
                    specialization = new Specialization();
                    specialization.setSpecializationName(specializationName);
                    specialization = specializationRepository.save(specialization);
                }

                Doctor doctor = new Doctor();
                doctor.setFirstName(doctorDTO.getFirstName());
                doctor.setLastName(doctorDTO.getLastName());
                doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
                doctor.setGender(doctorDTO.getGender());
                doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
                doctor.setEmail(doctorDTO.getEmail());
                doctor.setSpecialization(specialization);

                doctorRepository.save(doctor);
            }
            GenericResponse response = new GenericResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setResponseCode(DOCTOR_SUBMIT_SUCCESS);
            response.setResponseMessage(messageSource.getMessage(DOCTOR_SUBMIT_SUCCESS, null, Locale.ENGLISH));

            return response;
        }catch (GenericException e) {
            logger.error("Failed to create doctor email {}  exception{}", doctorDTO.getEmail(), e.getMessage());
            throw e;

        }
        catch (Exception ex) {
            logger.error("Failed to create doctor{} exception{}", doctorDTO.getEmail(), ex.getMessage());
            throw new GenericException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), messageSource.getMessage(DOCTOR_CREATE_FAIL, null, Locale.ENGLISH));

        }
    }
    @Transactional
    @Override
    public GenericResponse updateDoctor(Long id, DoctorDTO doctorDTO){

        logger.info("API call to update a doctor{}", doctorDTO);
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage(DOCTOR_NOT_FOUND, null, Locale.ENGLISH)));


        String specializationName = doctorDTO.getSpecialization().getSpecializationName();
        Specialization specialization = specializationRepository.findBySpecializationName(specializationName);
        if (specialization == null) {
            logger.info("new specialization is created{}", specialization);
            specialization = new Specialization();
            specialization.setSpecializationName(doctorDTO.getSpecialization().getSpecializationName());
            specializationRepository.save(specialization);
        }
        existingDoctor.setFirstName(doctorDTO.getFirstName());
        existingDoctor.setLastName(doctorDTO.getLastName());
        existingDoctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        existingDoctor.setGender(doctorDTO.getGender());
        existingDoctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        existingDoctor.setEmail(doctorDTO.getEmail());
        existingDoctor.setSpecialization(specialization);
        doctorRepository.save(existingDoctor);

        GenericResponse response=new GenericResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setResponseCode(DOCTOR_UPDATE_SUCCESS);
        response.setResponseMessage(messageSource.getMessage(DOCTOR_UPDATE_SUCCESS, null, Locale.ENGLISH));

        return response;
    }

    @Override
    public List<Doctor> getAllDoctors() {

        logger.info("API call to get a list of doctors");
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(Long doctorId) {

        logger.info("API call to get a list of doctors by id {}", doctorId);
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        if(doctor.isEmpty()) {
            throw new NotFoundException(messageSource.getMessage(DOCTOR_NOT_FOUND, null, Locale.ENGLISH));
        }
        return doctor;
    }

    @Override
    public List<Doctor> getDoctors(Integer pageNo, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Doctor> pagingDoctor = doctorRepository.findAll(pageRequest);
        return pagingDoctor.getContent();
    }

    @Override
    public Page<Doctor> findByFilter(String firstName, String lastName, LocalDate dateOfBirth, String gender, String phoneNumber, String email, Pageable pageable) {
        return doctorRepository.findByFilters(firstName, lastName, dateOfBirth, gender, phoneNumber, email, pageable);
    }

    @Override
    public Page<Doctor> findByPageableFilter(String firstName, String lastName, String phoneNumber, String email, Pageable pageable) {
        return doctorRepository.findByFirstNameOrLastNameOrPhoneNumberOrEmail(firstName, lastName, phoneNumber, email, pageable);
    }
}