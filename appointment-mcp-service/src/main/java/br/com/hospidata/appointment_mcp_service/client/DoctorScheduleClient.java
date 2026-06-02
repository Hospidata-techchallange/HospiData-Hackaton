package br.com.hospidata.appointment_mcp_service.client;

import br.com.hospidata.appointment_mcp_service.config.FeignAuthConfig;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.doctor_schedule.DoctorScheduleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "appointment-service",
        contextId = "doctorScheduleClient",
        configuration = FeignAuthConfig.class
)
public interface DoctorScheduleClient {

    @GetMapping("/api/v1/appointment/doctor-schedule")
    List<DoctorScheduleResponse> getAllDoctorSchedules();

    @GetMapping("/api/v1/appointment/doctor-schedule/filter")
    PageResponse<DoctorScheduleResponse> getDoctorSchedulesByDoctorId(@RequestParam("search") String search);

    @GetMapping("/api/v1/appointment/doctor-schedule/filter")
    PageResponse<DoctorScheduleResponse> getDoctorSchedulesByCategoryId(@RequestParam("search") String search);





}
