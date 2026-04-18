package br.com.hospidata.appointment_service.controller;

import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleResponse;
import br.com.hospidata.appointment_service.entity.DoctorSchedule;
import br.com.hospidata.appointment_service.service.DoctorScheduleService;
import jakarta.ws.rs.PUT;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointment/doctor-schedule")
public class DoctorScheduleController {

    private final DoctorScheduleService service;

    public DoctorScheduleController(DoctorScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DoctorScheduleResponse> createDoctorSchedule(@RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDoctorSchedule(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorScheduleResponse>> findAllDoctorSchedule(
            @RequestParam(required = false) Boolean active
            ) {
        return ResponseEntity.ok().body(service.findAllDoctorSchedule(active));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponse> getDoctorScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findDoctorScheduleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorScheduleById(@PathVariable UUID id) {
        service.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<Void> enableDoctorSchedule(@PathVariable UUID id) {
        service.enableDoctorSchedule(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponse> updateDoctorScheduleById(@PathVariable UUID id, @RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateDoctorSchedule(id, request));
    }

}
