package br.com.hospidata.appointment_mcp_service.client;


import br.com.hospidata.appointment_mcp_service.config.FeignAuthConfig;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.appointment.AppointmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "appointment-service",
        contextId = "appointmentClient",
        configuration = FeignAuthConfig.class
)
public interface AppointmentClient {


    @GetMapping("/api/v1/appointment/filter")
    PageResponse<AppointmentClientResponse> getAllAppointments();

    @GetMapping("/api/v1/appointment/filter")
    PageResponse<AppointmentClientResponse> getAppointmentsByAppointmentDateAndDoctorId(
            @RequestParam("search") String search
    );

    @PostMapping("/api/v1/appointment")
    AppointmentClientResponse createAppointment( @RequestBody AppointmentRequest request);

}
