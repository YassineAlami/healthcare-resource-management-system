package ma.hrms.healthcareresourcemanagementsystem.service;


import ma.hrms.healthcareresourcemanagementsystem.dto.SpecialtyDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;

import java.util.List;
import java.util.Optional;
public interface SpecialtyService {

    SpecialtyDTO saveOrUpdateSpecialty(SpecialtyDTO specialtyDTO);

    Optional<SpecialtyDTO> findSpecialtyById(Long id);

    Optional<SpecialtyDTO> findSpecialtyByName(String name);

    List<SpecialtyDTO> findAllSpecialties();

    void deleteSpecialty(Long id);
}
