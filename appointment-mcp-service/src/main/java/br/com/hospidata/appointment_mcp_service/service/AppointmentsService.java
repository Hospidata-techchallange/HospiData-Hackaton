package br.com.hospidata.appointment_mcp_service.service;


import br.com.hospidata.appointment_mcp_service.client.AppointmentClient;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentRequest;
import br.com.hospidata.appointment_mcp_service.dto.appointment.DoctorAvailableSlotsResponse;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
public class AppointmentsService {

    private final AppointmentClient client;

    public AppointmentsService(AppointmentClient client) {
        this.client = client;
    }

    public PageResponse<AppointmentClientResponse> getAllAppointments() {
        return client.getAllAppointments();
    }

    public PageResponse<AppointmentClientResponse> getAppointmentsByAppointmentDateAndDoctorId(
            String doctorId , String appointmentDate
    ) {

        System.out.println("doctorId: " + doctorId);
        System.out.println("appointmentDate: " + appointmentDate);

        String search = doctorId != null ? "doctor.id==" + doctorId : "" ;
        search = appointmentDate != null ? search + ";appointmentDate==" + appointmentDate : search;
        return client.getAppointmentsByAppointmentDateAndDoctorId(search);

    }

    public DoctorAvailableSlotsResponse getAvailableSlotsByDoctorAndDate(
            String doctorId,
            String appointmentDate
    ) {
        String normalizedDoctorId = requireValidDoctorId(doctorId);
        String normalizedAppointmentDate = requireValidAppointmentDate(appointmentDate);

        try {
            return client.getAvailableSlotsByDoctorAndDate(normalizedDoctorId, normalizedAppointmentDate);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException(
                    "Nao foram encontrados horarios disponiveis para o medico e data informados.",
                    e
            );
        } catch (FeignException e) {
            throw new IllegalStateException(
                    "Erro ao consultar horarios disponiveis no appointment-service. Status HTTP: " + e.status(),
                    e
            );
        }
    }

    public AppointmentClientResponse createAppointment(
            String doctorId ,
            String appointmentDate ,
            String startTime,
            String notes
    ) {
        AppointmentRequest request = new AppointmentRequest(
                UUID.fromString(doctorId) ,
                LocalDate.parse(appointmentDate),
                LocalTime.parse(startTime),
                notes
        );

        return client.createAppointment(request);
    }

    private String requireValidDoctorId(String doctorId) {
        if (doctorId == null || doctorId.isBlank()) {
            throw new IllegalArgumentException("doctorId e obrigatorio para consultar horarios disponiveis.");
        }

        String normalizedDoctorId = doctorId.trim();
        try {
            UUID.fromString(normalizedDoctorId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("doctorId deve ser um UUID valido.", e);
        }

        return normalizedDoctorId;
    }

    private String requireValidAppointmentDate(String appointmentDate) {
        if (appointmentDate == null || appointmentDate.isBlank()) {
            throw new IllegalArgumentException("appointmentDate e obrigatoria para consultar horarios disponiveis.");
        }

        String normalizedAppointmentDate = appointmentDate.trim();
        try {
            LocalDate.parse(normalizedAppointmentDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("appointmentDate deve estar no formato yyyy-MM-dd.", e);
        }

        return normalizedAppointmentDate;
    }

}
