package br.com.hospidata.appointment_service.controller;

import br.com.hospidata.appointment_service.controller.dto.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.hospidata.appointment_service.controller.dto.AppointmentRequest;
import br.com.hospidata.appointment_service.controller.dto.AppointmentResponse;
import br.com.hospidata.appointment_service.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody AppointmentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAppointment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAppointmentById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<AppointmentResponse>> filterAppointment(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.filterAppointment(search , pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStatus (
            @PathVariable UUID id ,
            @RequestBody RequestStatus status
    ) {
        service.updateStatus(id , status);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentById(
            @PathVariable UUID id
    ) {
        service.deleteAppointment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointmentById (
            @PathVariable UUID id,
            @RequestBody AppointmentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateAppointment(id , request));
    }

}
