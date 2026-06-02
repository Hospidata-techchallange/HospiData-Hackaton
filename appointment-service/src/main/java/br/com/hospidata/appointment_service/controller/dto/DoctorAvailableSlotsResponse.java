package br.com.hospidata.appointment_service.controller.dto;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public record DoctorAvailableSlotsResponse(
        UUID doctorId,
        String doctorName,
        String appointmentDate,
        DayOfWeek dayOfWeek,
        List<String> availableSlots
) {
}
