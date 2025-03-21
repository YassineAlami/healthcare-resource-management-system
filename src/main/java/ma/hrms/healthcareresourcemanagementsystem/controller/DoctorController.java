package ma.hrms.healthcareresourcemanagementsystem.controller;

import ma.hrms.healthcareresourcemanagementsystem.dto.DoctorDTO;
import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Doctor;
import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;
import ma.hrms.healthcareresourcemanagementsystem.service.DoctorService;
import ma.hrms.healthcareresourcemanagementsystem.service.SpecialtyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;



import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createOrUpdateDoctor(@RequestBody DoctorDTO doctorDTO) {
        DoctorDTO savedDoctor = doctorService.saveOrUpdateDoctor(doctorDTO);
        return ResponseEntity.ok(savedDoctor);
    }

    @PutMapping("/{doctorId}/specialty")
    public ResponseEntity<DoctorDTO> assignSpecialty(
            @PathVariable Long doctorId,
            @RequestParam String specialtyName) {
        DoctorDTO updatedDoctor = doctorService.assignSpecialty(doctorId, specialtyName);
        return ResponseEntity.ok(updatedDoctor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return doctorService.findDoctorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@PathVariable String email) {
        return doctorService.findDoctorByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/specialty/{specialtyName}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialty(@PathVariable String specialtyName) {
        List<DoctorDTO> doctors = doctorService.findDoctorsBySpecialty(specialtyName);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.findAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{firstName}/{lastName}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByName(@PathVariable String firstName, @PathVariable String lastName) {
        List<DoctorDTO> doctors = doctorService.findDoctorsByName(firstName, lastName);
        return ResponseEntity.ok(doctors);
    }





    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<List<PatientDTO>> getDoctorPatients(@PathVariable Long doctorId) {
        List<PatientDTO> patients = doctorService.getPatientsByDoctor(doctorId);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{doctorId}/patients/paged")
    public ResponseEntity<List<PatientDTO>> getDoctorPatientsWithPagination(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<PatientDTO> patients = doctorService.getPatientsByDoctorWithPagination(doctorId, pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{doctorId}/patients/count")
    public ResponseEntity<Long> getDoctorPatientCount(@PathVariable Long doctorId) {
        long count = doctorService.getPatientCountForDoctor(doctorId);
        return ResponseEntity.ok(count);
    }





    @GetMapping("/{doctorId}/patients/search")
    public ResponseEntity<?> searchPatient(
            @PathVariable Long doctorId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(defaultValue = "false") boolean includeDetails) {

        if (includeDetails) {
            Optional<PatientDTO> patient = doctorService.findPatientByNameForDoctor(doctorId, firstName, lastName);
            return patient
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            boolean exists = doctorService.hasPatientWithName(doctorId, firstName, lastName);
            return ResponseEntity.ok(exists);
        }
    }




}
