package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.client.DoctorScheduleClient;
import br.com.hospidata.appointment_mcp_service.dto.PageResponse;
import br.com.hospidata.appointment_mcp_service.dto.doctor_schedule.DoctorScheduleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleClient client;


    public DoctorScheduleService(DoctorScheduleClient client) {
        this.client = client;
    }

    public List<DoctorScheduleResponse> getAllDoctorSchedules() { return client.getAllDoctorSchedules(); }

    public PageResponse<DoctorScheduleResponse> getDoctorSchedulesByDoctorId(String doctorId) {
        String search = "doctor.id==" + doctorId;
        return client.getDoctorSchedulesByDoctorId(search);
    }

    public PageResponse<DoctorScheduleResponse> getDoctorSchedulesByCategoryId(String categoryId) {
        String search = "doctor.categories.id==" + categoryId;
        return client.getDoctorSchedulesByCategoryId(search);
    }


}
