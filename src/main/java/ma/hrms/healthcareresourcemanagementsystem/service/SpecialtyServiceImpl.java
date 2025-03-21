package ma.hrms.healthcareresourcemanagementsystem.service;

import ma.hrms.healthcareresourcemanagementsystem.dto.SpecialtyDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;
import ma.hrms.healthcareresourcemanagementsystem.repository.SpecialtyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, ModelMapper modelMapper) {
        this.specialtyRepository = specialtyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SpecialtyDTO saveOrUpdateSpecialty(SpecialtyDTO specialtyDTO) {
        Specialty specialty = modelMapper.map(specialtyDTO, Specialty.class);
        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return modelMapper.map(savedSpecialty, SpecialtyDTO.class);
    }

    @Override
    public Optional<SpecialtyDTO> findSpecialtyById(Long id) {
        return specialtyRepository.findById(id)
                .map(specialty -> modelMapper.map(specialty, SpecialtyDTO.class));
    }

    @Override
    public Optional<SpecialtyDTO> findSpecialtyByName(String name) {
        return specialtyRepository.findByName(name)
                .map(specialty -> modelMapper.map(specialty, SpecialtyDTO.class));
    }

    @Override
    public List<SpecialtyDTO> findAllSpecialties() {
        return specialtyRepository.findAll().stream()
                .map(specialty -> modelMapper.map(specialty, SpecialtyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSpecialty(Long id) {
        specialtyRepository.deleteById(id);
    }
}
