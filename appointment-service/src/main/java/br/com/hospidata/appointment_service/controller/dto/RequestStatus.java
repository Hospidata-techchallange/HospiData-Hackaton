package br.com.hospidata.appointment_service.controller.dto;

import br.com.hospidata.appointment_service.entity.enums.AppointmentStatus;

public record RequestStatus(
       AppointmentStatus status
) {
}
