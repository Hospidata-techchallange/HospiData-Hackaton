package br.com.hospidata.appointment_service.controller;

import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleRequest;
import br.com.hospidata.appointment_service.controller.dto.DoctorScheduleResponse;
import br.com.hospidata.appointment_service.service.DoctorScheduleService;
import br.com.hospidata.common_security.aspect.CheckRole;
import br.com.hospidata.common_security.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    @CheckRole({Role.ADMIN , Role.DOCTOR})
    public ResponseEntity<DoctorScheduleResponse> createDoctorSchedule(@RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDoctorSchedule(request));
    }

    @GetMapping
    @CheckRole({Role.ADMIN , Role.AGENT , Role.DOCTOR})
    public ResponseEntity<List<DoctorScheduleResponse>> findAllDoctorSchedule(
            @RequestParam(required = false) Boolean active
            ) {
        return ResponseEntity.ok().body(service.findAllDoctorSchedule(active));
    }

    @GetMapping("/{id}")
    @CheckRole({Role.ADMIN , Role.AGENT , Role.DOCTOR})
    public ResponseEntity<DoctorScheduleResponse> getDoctorScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.findDoctorScheduleById(id));
    }

    @GetMapping("/filter")
    @CheckRole({Role.ADMIN , Role.AGENT , Role.DOCTOR})
    public ResponseEntity<Page<DoctorScheduleResponse>> filterDoctorSchedule(
        @RequestParam(required = false) String search,
        Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.filterDoctorSchedule(search , pageable));
    }

    @DeleteMapping("/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<Void> deleteDoctorScheduleById(@PathVariable UUID id) {
        service.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<Void> enableDoctorSchedule(@PathVariable UUID id) {
        service.enableDoctorSchedule(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @CheckRole({Role.ADMIN})
    public ResponseEntity<DoctorScheduleResponse> updateDoctorScheduleById(@PathVariable UUID id, @RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateDoctorSchedule(id, request));
    }

}
