package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.client.DoctorClient;
import br.com.hospidata.appointment_mcp_service.dto.doctor.DoctorClientResponse;
import br.com.hospidata.appointment_mcp_service.mapper.DoctorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorClient client;
    private final DoctorMapper mapper;

    public DoctorService(DoctorClient client, DoctorMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public List<DoctorClientResponse> getAllDoctors() {
        return client.getAllDoctors();
    }

}
