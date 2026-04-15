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
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findDoctorById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable UUID id) {
        service.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<Void> enableDoctor(@PathVariable UUID id) {
        service.enableDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctorById(@PathVariable UUID id, @RequestBody DoctorRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateDoctor(id, request));
    }


}
