package ma.hrms.healthcareresourcemanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "specialties")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor @Builder
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ToString.Exclude  // Add this annotation
    @OneToMany(mappedBy = "specialty")
    private List<Doctor> doctors;


    // Override toString to prevent infinite loop
    @Override
    public String toString() {
        return "Specialty(id=" + id +
                ", name=" + name + ")";
    }

}
