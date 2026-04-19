package br.com.hospidata.appointment_service.service;

import br.com.hospidata.appointment_service.common.CustomRsqlVisitor;
import br.com.hospidata.appointment_service.controller.dto.AppointmentRequest;
import br.com.hospidata.appointment_service.controller.dto.AppointmentResponse;
import br.com.hospidata.appointment_service.controller.dto.RequestStatus;
import br.com.hospidata.appointment_service.entity.Appointment;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import br.com.hospidata.appointment_service.entity.enums.AppointmentStatus;
import br.com.hospidata.appointment_service.mapper.AppointmentMapper;
import br.com.hospidata.appointment_service.repository.AppointmentRespository;
import br.com.hospidata.common.exceptions.BadRequestException;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import cz.jirutka.rsql.parser.RSQLParser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRespository repository;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final DoctorService doctorService;
    private final AppointmentMapper mapper;


    public AppointmentService(AppointmentRespository repository, DoctorAvailabilityService doctorAvailabilityService, DoctorService doctorService, AppointmentMapper mapper) {
        this.repository = repository;
        this.doctorAvailabilityService = doctorAvailabilityService;
        this.doctorService = doctorService;
        this.mapper = mapper;
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        Doctor doctor = doctorService.findDoctorEntityById(request.doctorId());

        DayOfWeek dayOfWeek = request.appointmentDate().getDayOfWeek();

        doctorAvailabilityService.getSchedules(doctor.getId(), dayOfWeek);
        DoctorSchedule schedule = doctorAvailabilityService.validateAndGetSchedule(
                doctor.getId(),
                dayOfWeek,
                request.startTime()
        );

        validateAppointmentConflict(
                doctor.getId(),
                request.appointmentDate(),
                request.startTime()
        );

        var result = repository.save(mapper.toEntity(request, doctor, schedule));
        return mapper.toResponse(result);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAllAppointmentsByDoctorId(UUID doctorId , Boolean active){
        if (active == null){
            return mapper.toResponses(repository.findByDoctorId(doctorId));
        }
        return mapper.toResponses(repository.findByDoctorIdAndActive(doctorId , active));
    }

    private void validateAppointmentConflict(UUID doctorId, LocalDate appointmentDate, LocalTime startTime) {
        boolean alreadyExists = repository.existsByDoctorIdAndAppointmentDateAndStartTimeAndActiveTrue(
                doctorId,
                appointmentDate,
                startTime
        );

        if (alreadyExists) {
            throw new BadRequestException(
                    "Appointment",
                    "startTime",
                    String.format(
                            "There is already an appointment for doctor %s on %s at %s",
                            doctorId,
                            appointmentDate,
                            startTime
                    )
            );
        }
    }


    public Page<AppointmentResponse> filterAppointment(String search, Pageable pageable) {
        Specification<Appointment> spec = null;

        if (search != null && !search.isBlank()) {
            spec = new RSQLParser()
                    .parse(search)
                    .accept(new CustomRsqlVisitor<>());
        }

        return repository
                .findAll(spec, pageable)
                .map(mapper::toResponse);
    }

    public void updateStatus(UUID id, RequestStatus status){
        Appointment appointment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id.toString()));
        appointment.setStatus(status.status());
        repository.save(appointment);
    }

    public AppointmentResponse findAppointmentById(UUID id) {
        Appointment find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id.toString()));
        return mapper.toResponse(find);
    }

    public void deleteAppointment(UUID id) {
        Appointment find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id.toString()));
        find.setActive(false);
        find.setStatus(AppointmentStatus.CANCELLED);
        repository.save(find);
    }

    public AppointmentResponse updateAppointment(UUID id, AppointmentRequest request) {
        Doctor doctor = doctorService.findDoctorEntityById(request.doctorId());

        DayOfWeek dayOfWeek = request.appointmentDate().getDayOfWeek();

        doctorAvailabilityService.getSchedules(doctor.getId(), dayOfWeek);
        DoctorSchedule schedule = doctorAvailabilityService.validateAndGetSchedule(
                doctor.getId(),
                dayOfWeek,
                request.startTime()
        );

        validateAppointmentConflict(
                doctor.getId(),
                request.appointmentDate(),
                request.startTime()
        );

        Appointment find = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id.toString()));

        find.setDoctor(doctor);
        find.setAppointmentDate(request.appointmentDate());
        find.setStartTime(request.startTime());
        find.setEndTime(request.startTime().plusMinutes(schedule.getSlotDurationMinutes()));
        find.setNotes(request.notes());

        return mapper.toResponse(repository.save(find));
    }

}
