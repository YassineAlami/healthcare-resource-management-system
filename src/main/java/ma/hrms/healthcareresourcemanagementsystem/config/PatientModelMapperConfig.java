package ma.hrms.healthcareresourcemanagementsystem.config;

import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Patient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class PatientModelMapperConfig {

    @Bean(name = "patientModelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Basic mapping without complexity
        modelMapper.typeMap(Patient.class, PatientDTO.class)
                .addMapping(src -> src.getDoctor().getId(), PatientDTO::setDoctorId);

        return modelMapper;
    }
}
