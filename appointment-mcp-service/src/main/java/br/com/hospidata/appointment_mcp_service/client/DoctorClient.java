package br.com.hospidata.appointment_mcp_service.client;


import br.com.hospidata.appointment_mcp_service.config.FeignAuthConfig;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.doctor.DoctorClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "appointment-service",
        contextId = "doctorClient",
        configuration = FeignAuthConfig.class
)
public interface DoctorClient {

    @GetMapping("/api/v1/appointment/doctor")
    List<DoctorClientResponse> getAllDoctors();

    @GetMapping("/api/v1/appointment/doctor/filter")
    PageResponse<DoctorClientResponse> getAllDoctorsByCategoryId(@RequestParam("search") String search);


}
