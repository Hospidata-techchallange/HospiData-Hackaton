package br.com.hospidata.appointment_service.controller;


import br.com.hospidata.appointment_service.controller.dto.DoctorRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorResponse;
import br.com.hospidata.appointment_service.entity.Doctor;
import br.com.hospidata.appointment_service.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment/doctor")
public class DoctorController {

    private DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @RequestBody DoctorRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDoctor(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctor(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok().body(service.findAllDoctors(active));
    }



}
