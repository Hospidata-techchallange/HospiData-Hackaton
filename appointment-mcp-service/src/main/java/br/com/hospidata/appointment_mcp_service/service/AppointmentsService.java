package br.com.hospidata.appointment_mcp_service.service;


import br.com.hospidata.appointment_mcp_service.client.AppointmentClient;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

}
