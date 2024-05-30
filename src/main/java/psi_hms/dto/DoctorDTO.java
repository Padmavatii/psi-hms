package psi_hms.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import psi_hms.entity.Specialization;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {

    private Long doctorId;

    @NotBlank(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "last name cannot be null")
    @Size(min = 2, max = 50, message = "Second name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]*$", message = "Phone number must be valid")
    @Size(min =10, max = 10, message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private Specialization specialization;

}
