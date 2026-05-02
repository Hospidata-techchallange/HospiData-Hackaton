package br.com.hospidata.appointment_mcp_service.dto.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentRequest(
        UUID doctorId,
        LocalDate appointmentDate,
        LocalTime startTime,
        String notes
) {
}
