package ma.hrms.healthcareresourcemanagementsystem.controller;

import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Patient;
import ma.hrms.healthcareresourcemanagementsystem.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createOrUpdatePatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO savedPatient = patientService.saveOrUpdatePatient(patientDTO);
        return ResponseEntity.ok(savedPatient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return patientService.findPatientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.findAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PatientDTO>> getPatientsByDoctorId(@PathVariable Long doctorId) {
        List<PatientDTO> patients = patientService.findPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(patients);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }






    @PutMapping("/{patientId}/doctor/{doctorId}")
    public ResponseEntity<PatientDTO> assignDoctor(
            @PathVariable Long patientId,
            @PathVariable Long doctorId) {
        PatientDTO updatedPatient = patientService.assignPatientToDoctor(patientId, doctorId);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{patientId}/doctor")
    public ResponseEntity<PatientDTO> removeDoctor(@PathVariable Long patientId) {
        PatientDTO updatedPatient = patientService.removePatientFromDoctor(patientId);
        return ResponseEntity.ok(updatedPatient);
    }
}
