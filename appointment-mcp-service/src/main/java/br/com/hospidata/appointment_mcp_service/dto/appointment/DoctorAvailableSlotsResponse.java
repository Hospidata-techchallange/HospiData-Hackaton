package br.com.hospidata.appointment_mcp_service.dto.appointment;

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
