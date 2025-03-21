package ma.hrms.healthcareresourcemanagementsystem.controller;

import ma.hrms.healthcareresourcemanagementsystem.dto.SpecialtyDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;
import ma.hrms.healthcareresourcemanagementsystem.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @PostMapping
    public ResponseEntity<SpecialtyDTO> createOrUpdateSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        SpecialtyDTO savedSpecialty = specialtyService.saveOrUpdateSpecialty(specialtyDTO);
        return ResponseEntity.ok(savedSpecialty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDTO> getSpecialtyById(@PathVariable Long id) {
        return specialtyService.findSpecialtyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SpecialtyDTO> getSpecialtyByName(@PathVariable String name) {
        return specialtyService.findSpecialtyByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        List<SpecialtyDTO> specialties = specialtyService.findAllSpecialties();
        return ResponseEntity.ok(specialties);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return ResponseEntity.noContent().build();
    }
}
