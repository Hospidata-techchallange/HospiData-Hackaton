package br.com.hospidata.appointment_service.service;

import br.com.hospidata.appointment_service.common.CustomRsqlVisitor;
import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleResponse;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import br.com.hospidata.appointment_service.mapper.DoctorScheduleMapper;
import br.com.hospidata.appointment_service.repository.DoctorScheduleRepository;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import cz.jirutka.rsql.parser.RSQLParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository repository;
    private final DoctorService doctorService;
    private final DoctorScheduleMapper mapper;


    public DoctorScheduleService(DoctorScheduleRepository repository, DoctorService doctorService, DoctorScheduleMapper mapper) {
        this.repository = repository;
        this.doctorService = doctorService;
        this.mapper = mapper;
    }

    @Transactional
    public DoctorScheduleResponse createDoctorSchedule(DoctorScheduleRequest request ) {

        var doctor = doctorService.findDoctorEntityById(request.doctorId());

        var result = repository.save(mapper.toEntity(request , doctor));
        return mapper.toResponse(result);
    }

    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> findAllDoctorSchedule(Boolean active){
        if (active == null){
            return mapper.toResponses(repository.findAll());
        }
        return mapper.toResponses(repository.findByActive(active));
    }

    @Transactional(readOnly = true)
    public DoctorScheduleResponse findDoctorScheduleById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorSchedule", "id", id.toString())));
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        DoctorSchedule find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString()));
        repository.delete(find);
    }

    @Transactional
    public void enableDoctorSchedule(UUID id){
        DoctorSchedule find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id.toString()));
        find.setActive(true);
        repository.save(find);
    }

    @Transactional
    public DoctorScheduleResponse updateDoctorSchedule(UUID id, DoctorScheduleRequest request){

        var doctor = doctorService.findDoctorEntityById(request.doctorId());

        DoctorSchedule find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DoctorSchedule", "id", id.toString()));

        updateDoctorScheduleFields(find , request , doctor);

        return mapper.toResponse(repository.save(find));
    }

    private void updateDoctorScheduleFields(DoctorSchedule doctorSchedule , DoctorScheduleRequest request , Doctor doctor){
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setDayOfWeek(request.dayOfWeek());
        doctorSchedule.setStartTime(request.startTime());
        doctorSchedule.setEndTime(request.endTime());
        doctorSchedule.setSlotDurationMinutes(request.slotDurationMinutes());
    }

    @Transactional(readOnly = true)
    public Page<DoctorScheduleResponse> filterDoctorSchedule(String search, Pageable pageable) {
        Specification<DoctorSchedule> spec = null;

        if (search != null && !search.isBlank()) {
            spec = new RSQLParser()
                    .parse(search)
                    .accept(new CustomRsqlVisitor<>());
        }

        return repository
                .findAll(spec, pageable)
                .map(mapper::toResponse);

    }
}
