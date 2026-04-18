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
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorRepository repository;
    private final CategoryService categoryService;
    private final DoctorMapper mapper;

    public DoctorService(DoctorRepository repository, CategoryService categoryService, DoctorMapper mapper) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.mapper = mapper;
    }


    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {

        var categories = categoryService.findCategoriesByIds(request.categoryIds());

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

    @Transactional(readOnly = true)
    public DoctorResponse findDoctorById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString())));
    }

    @Transactional(readOnly = true)
    public Doctor findDoctorEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString()));
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        Doctor find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString()));
        find.setActive(false);
        repository.save(find);
    }

    @Transactional
    public void enableDoctor(UUID id) {
        Doctor find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString()));
        find.setActive(true);
        repository.save(find);
    }

    @Transactional
    public DoctorResponse updateDoctor(UUID id, DoctorRequest request) {

        var categories = categoryService.findCategoriesByIds(request.categoryIds());

        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString()));

        updateDoctorFields(doctor, request, categories);

        return mapper.toResponse(repository.save(doctor));
    }

    private void updateDoctorFields(Doctor doctor, DoctorRequest request, List<Category> categories) {
        doctor.setName(request.name());
        doctor.setCrm(request.crm());
        doctor.setCrmUf(request.crmUf());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setBirthDate(request.birthDate());
        doctor.setCategories(new HashSet<>(categories));
    }

}
