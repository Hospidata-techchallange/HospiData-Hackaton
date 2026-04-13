package br.com.hospidata.appointment_service.mapper;

import br.com.hospidata.appointment_service.controller.dto.DoctorRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorResponse;
import br.com.hospidata.appointment_service.entity.Category;
import br.com.hospidata.appointment_service.entity.Doctor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    public CategoryMapper categoryMapper;

    public DoctorMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public Doctor toEntity(DoctorRequest request , List<Category> categories) {

        Doctor doctor = new Doctor();

        doctor.setName(request.name());
        doctor.setCrm(request.crm());
        doctor.setCrmUf(request.crmUf());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setBirthDate(request.birthDate());
        doctor.setCategories(new HashSet<>(categories));

        return doctor;
    }

    public DoctorResponse toResponse(Doctor doctor) {

        return new DoctorResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getCrm(),
                doctor.getCrmUf(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getBirthDate(),
                doctor.getActive(),
                doctor.getCreatedAt(),
                doctor.getLastUpdatedAt(),
                categoryMapper.toResponses(new ArrayList<>(doctor.getCategories()))
        );
    }

    public List<DoctorResponse> toResponses(List<Doctor> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }








}
