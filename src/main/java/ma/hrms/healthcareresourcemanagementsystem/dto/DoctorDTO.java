package ma.hrms.healthcareresourcemanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
    private String phoneNumber;
    private Boolean gender;
    private int yearsOfExperience;
    private String specialtyName;
}
