package br.com.hospidata.appointment_service.service;

import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import br.com.hospidata.appointment_service.repository.DoctorRepository;
import br.com.hospidata.appointment_service.repository.DoctorScheduleRepository;
import br.com.hospidata.common.exceptions.BadRequestException;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorAvailabilityService {

    private final DoctorScheduleRepository repository;


    public DoctorAvailabilityService(DoctorScheduleRepository repository) {
        this.repository = repository;
    }

    public List<DoctorSchedule> getSchedules(UUID doctorId , DayOfWeek dayOfWeek) {

        List<DoctorSchedule> doctorSchedules = repository.findByDoctorIdAndDayOfWeekAndActiveTrue(doctorId, dayOfWeek);

        if (doctorSchedules.isEmpty()) {
            throw new ResourceNotFoundException("DoctorSchedule", "doctorId + dayOfWeek", String.format("%s + %s", doctorId, dayOfWeek));
        }

        return doctorSchedules;
    }

    public DoctorSchedule validateAndGetSchedule(UUID doctorId , DayOfWeek dayOfWeek , LocalTime requestedTime) {
        List<DoctorSchedule> schedules = getSchedules(doctorId, dayOfWeek);

        return schedules.stream()
                .filter(schedule -> isValidSlot(schedule, requestedTime))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("DoctorSchedule" , "requestedTime" , String.format("Invalid appointment time : %s", requestedTime)));

    }

    private boolean isValidSlot(DoctorSchedule schedule, LocalTime requestedTime) {

        if (requestedTime.isBefore(schedule.getStartTime())) return false;

        if (!requestedTime.isBefore(schedule.getEndTime())) return false;

        long minutes = java.time.Duration
                .between(schedule.getStartTime(), requestedTime)
                .toMinutes();

        if (minutes % schedule.getSlotDurationMinutes() != 0) return false;

        return !requestedTime
                .plusMinutes(schedule.getSlotDurationMinutes())
                .isAfter(schedule.getEndTime());
    }








}
