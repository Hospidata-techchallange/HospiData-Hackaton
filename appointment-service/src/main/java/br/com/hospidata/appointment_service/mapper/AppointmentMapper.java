package br.com.hospidata.appointment_service.mapper;

import br.com.hospidata.appointment_service.controller.dto.AppointmentRequest;
import br.com.hospidata.appointment_service.controller.dto.AppointmentResponse;
import br.com.hospidata.appointment_service.entity.Appointment;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    private final DoctorMapper doctorMapper;

    public AppointmentMapper(DoctorMapper doctorMapper) {
        this.doctorMapper = doctorMapper;
    }


    public Appointment toEntity(AppointmentRequest request , Doctor doctor , DoctorSchedule doctorSchedule) {
        Appointment appointment = new Appointment();

        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.startTime().plusMinutes(doctorSchedule.getSlotDurationMinutes()));
        appointment.setNotes(request.notes());

        return appointment;
    }

    public AppointmentResponse toResponse(Appointment entity) {
        return new AppointmentResponse(
                entity.getId(),
                doctorMapper.toSimpleResponse(entity.getDoctor()),
                entity.getAppointmentDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getLastUpdatedAt()
        );
    }

    public List<AppointmentResponse> toResponses(List<Appointment> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
