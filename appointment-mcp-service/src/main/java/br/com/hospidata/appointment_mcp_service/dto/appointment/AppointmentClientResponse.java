package br.com.hospidata.appointment_mcp_service.dto.appointment;

import br.com.hospidata.appointment_mcp_service.dto.doctor_schedule.DoctorSimpleResponse;
import br.com.hospidata.appointment_mcp_service.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentClientResponse(
        UUID id,
        DoctorSimpleResponse doctor,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        String notes,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}