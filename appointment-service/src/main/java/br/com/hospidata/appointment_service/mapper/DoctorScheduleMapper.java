package br.com.hospidata.appointment_service.mapper;

import br.com.hospidata.appointment_service.controller.dto.DoctorResponse;
import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleResponse;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorScheduleMapper {

    private final DoctorMapper doctorMapper;

    public DoctorScheduleMapper(DoctorMapper doctorMapper) {
        this.doctorMapper = doctorMapper;
    }


    public DoctorSchedule toEntity(DoctorScheduleRequest request, Doctor doctor) {

        DoctorSchedule entity = new DoctorSchedule();

        entity.setDoctor(doctor);
        entity.setDayOfWeek(request.dayOfWeek());
        entity.setStartTime(request.startTime());
        entity.setEndTime(request.endTime());
        entity.setSlotDurationMinutes(request.slotDurationMinutes());

        return entity;

    }

    public DoctorScheduleResponse toResponse(DoctorSchedule entity) {

        return new DoctorScheduleResponse(
          entity.getId(),
          doctorMapper.toSimpleResponse(entity.getDoctor()),
          entity.getDayOfWeek(),
          entity.getStartTime(),
          entity.getEndTime(),
          entity.getSlotDurationMinutes(),
          entity.getActive(),
          entity.getCreatedAt(),
          entity.getLastUpdatedAt()
        );

    }

    public List<DoctorScheduleResponse> toResponses(List<DoctorSchedule> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
