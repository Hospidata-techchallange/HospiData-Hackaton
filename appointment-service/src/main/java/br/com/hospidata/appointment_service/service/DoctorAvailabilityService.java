package br.com.hospidata.appointment_service.service;

import br.com.hospidata.appointment_service.controller.dto.DoctorAvailableSlotsResponse;
import br.com.hospidata.appointment_service.entity.Appointment;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import br.com.hospidata.appointment_service.entity.enums.AppointmentStatus;
import br.com.hospidata.appointment_service.repository.AppointmentRespository;
import br.com.hospidata.appointment_service.repository.DoctorScheduleRepository;
import br.com.hospidata.common.exceptions.BadRequestException;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorAvailabilityService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final DoctorScheduleRepository repository;
    private final AppointmentRespository appointmentRespository;
    private final DoctorService doctorService;

    public DoctorAvailabilityService(
            DoctorScheduleRepository repository,
            AppointmentRespository appointmentRespository,
            DoctorService doctorService
    ) {
        this.repository = repository;
        this.appointmentRespository = appointmentRespository;
        this.doctorService = doctorService;
    }

    public List<DoctorSchedule> getSchedules(UUID doctorId, DayOfWeek dayOfWeek) {

        List<DoctorSchedule> doctorSchedules = repository.findByDoctorIdAndDayOfWeekAndActiveTrue(doctorId, dayOfWeek);

        if (doctorSchedules.isEmpty()) {
            throw new ResourceNotFoundException("DoctorSchedule", "doctorId + dayOfWeek", String.format("%s + %s", doctorId, dayOfWeek));
        }

        return doctorSchedules;
    }

    public DoctorSchedule validateAndGetSchedule(UUID doctorId, DayOfWeek dayOfWeek, LocalTime requestedTime) {
        List<DoctorSchedule> schedules = getSchedules(doctorId, dayOfWeek);

        return schedules.stream()
                .filter(schedule -> isValidSlot(schedule, requestedTime))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("DoctorSchedule", "requestedTime", String.format("Invalid appointment time : %s", requestedTime)));

    }

    @Transactional(readOnly = true)
    public DoctorAvailableSlotsResponse getAvailableSlotsByDoctorAndDate(String doctorId, String appointmentDate) {
        UUID doctorUuid = parseDoctorId(doctorId);
        LocalDate date = parseAppointmentDate(appointmentDate);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        Doctor doctor = doctorService.findDoctorEntityById(doctorUuid);
        List<DoctorSchedule> schedules = repository.findByDoctorIdAndDayOfWeekAndActiveTrue(doctorUuid, dayOfWeek);

        if (schedules.isEmpty()) {
            return new DoctorAvailableSlotsResponse(
                    doctor.getId(),
                    doctor.getName(),
                    date.format(DATE_FORMATTER),
                    dayOfWeek,
                    List.of()
            );
        }

        List<Appointment> bookedAppointments = appointmentRespository
                .findByDoctorIdAndAppointmentDateAndActiveTrueAndStatusNot(
                        doctorUuid,
                        date,
                        AppointmentStatus.CANCELLED
                );

        List<String> availableSlots = schedules.stream()
                .flatMap(schedule -> generateSlots(schedule, bookedAppointments).stream())
                .distinct()
                .sorted()
                .map(slot -> slot.format(TIME_FORMATTER))
                .toList();

        return new DoctorAvailableSlotsResponse(
                doctor.getId(),
                doctor.getName(),
                date.format(DATE_FORMATTER),
                dayOfWeek,
                availableSlots
        );
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

    private List<LocalTime> generateSlots(DoctorSchedule schedule, List<Appointment> bookedAppointments) {
        validateScheduleSlotDuration(schedule);

        LocalTime slot = schedule.getStartTime();
        List<LocalTime> slots = new ArrayList<>();

        while (!slot.plusMinutes(schedule.getSlotDurationMinutes()).isAfter(schedule.getEndTime())) {
            LocalTime slotStart = slot;
            LocalTime slotEnd = slot.plusMinutes(schedule.getSlotDurationMinutes());

            boolean hasConflict = bookedAppointments.stream()
                    .anyMatch(appointment -> overlaps(slotStart, slotEnd, appointment.getStartTime(), appointment.getEndTime()));

            if (!hasConflict) {
                slots.add(slotStart);
            }

            slot = slot.plusMinutes(schedule.getSlotDurationMinutes());
        }

        return slots.stream()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    private boolean overlaps(LocalTime slotStart, LocalTime slotEnd, LocalTime appointmentStart, LocalTime appointmentEnd) {
        return slotStart.isBefore(appointmentEnd) && slotEnd.isAfter(appointmentStart);
    }

    private UUID parseDoctorId(String doctorId) {
        try {
            return UUID.fromString(doctorId);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Doctor", "doctorId", "Invalid UUID: " + doctorId);
        }
    }

    private LocalDate parseAppointmentDate(String appointmentDate) {
        try {
            return LocalDate.parse(appointmentDate, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Appointment", "appointmentDate", "Invalid date format. Expected yyyy-MM-dd");
        }
    }

    private void validateScheduleSlotDuration(DoctorSchedule schedule) {
        if (schedule.getSlotDurationMinutes() == null || schedule.getSlotDurationMinutes() <= 0) {
            throw new BadRequestException(
                    "DoctorSchedule",
                    "slotDurationMinutes",
                    String.format("Invalid slot duration for schedule %s", schedule.getId())
            );
        }
    }
}
