package ma.hrms.healthcareresourcemanagementsystem.config;

import ma.hrms.healthcareresourcemanagementsystem.dto.DoctorDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Doctor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.Converter;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Basic configuration
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true)
                .setSkipNullEnabled(true);  // Add this to skip null values

        // Doctor to DoctorDTO mapping with null-safe conversion
        Converter<Doctor, String> specialtyToNameConverter = context -> {
            Doctor source = context.getSource();
            if (source != null && source.getSpecialty() != null) {
                return source.getSpecialty().getName();
            }
            return null;
        };

        // DoctorDTO to Doctor mapping
        modelMapper.createTypeMap(DoctorDTO.class, Doctor.class)
                .addMappings(mapper -> {
                    mapper.skip(Doctor::setSpecialty);
                    mapper.skip(Doctor::setPatients);
                });

        // Doctor to DoctorDTO mapping
        modelMapper.createTypeMap(Doctor.class, DoctorDTO.class)
                .addMappings(mapper -> {
                    mapper.using(specialtyToNameConverter)
                            .map(source -> source, DoctorDTO::setSpecialtyName);
                });

        return modelMapper;
    }
}