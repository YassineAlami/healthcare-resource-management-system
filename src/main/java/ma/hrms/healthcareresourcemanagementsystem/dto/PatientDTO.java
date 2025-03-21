package ma.hrms.healthcareresourcemanagementsystem.dto;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Long doctorId; // Expose only the doctor ID
}
