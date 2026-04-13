package br.com.hospidata.appointment_service.service;


import br.com.hospidata.appointment_service.controller.dto.DoctorRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorResponse;
import br.com.hospidata.appointment_service.entity.Category;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.mapper.DoctorMapper;
import br.com.hospidata.appointment_service.repository.CategoryRepository;
import br.com.hospidata.appointment_service.repository.DoctorRepository;
import br.com.hospidata.common.exceptions.BadRequestException;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository repository;
    private final CategoryRepository categoryRepository;
    private final DoctorMapper mapper;

    public DoctorService(DoctorRepository repository, CategoryRepository categoryRepository, DoctorMapper mapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }


    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {

        List<UUID> categoryIds;

        try {
            categoryIds = request.categoryIds()
                    .stream()
                    .map(UUID::fromString)
                    .toList();
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(
                    "Category",
                    "categoryIds",
                    "one or more UUIDs are invalid"
            );
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new ResourceNotFoundException("Category", "id", categoryIds.toString());
        }

        var result = repository.save(mapper.toEntity(request , categories));
        return mapper.toResponse(result);
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> findAllDoctors(Boolean active) {
        if (active == null) {
            return mapper.toResponses(repository.findAll());
        }
        return mapper.toResponses(repository.findByActive(active));

    }

}
