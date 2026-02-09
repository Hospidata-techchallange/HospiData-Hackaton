package br.com.hospidata.stock_service.controller;

import br.com.hospidata.common_security.aspect.CheckRole;
import br.com.hospidata.common_security.enums.Role;
import br.com.hospidata.stock_service.controller.dto.LocationRequest;
import br.com.hospidata.stock_service.controller.dto.LocationResponse;
import br.com.hospidata.stock_service.controller.dto.ProductRequest;
import br.com.hospidata.stock_service.controller.dto.ProductResponse;
import br.com.hospidata.stock_service.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock/location")
public class LocationContoller {

    private final LocationService service;

    public LocationContoller(LocationService service) {
        this.service = service;
    }

    @PostMapping
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<List<LocationResponse>> addLocations(
            @RequestBody List<LocationRequest> request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createLocations(request));
    }

    @GetMapping
    @CheckRole({Role.NURSE, Role.PHARMACIST, Role.ADMIN})
    public ResponseEntity<List<LocationResponse>> getAllLocations(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllLocations(active));
    }

    @GetMapping("/{id}")
    @CheckRole({Role.NURSE, Role.PHARMACIST, Role.ADMIN})
    public ResponseEntity<LocationResponse> getLocationsByid(
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findLocationById(id));
    }

    @DeleteMapping("/{id}")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<Void> deleteLocationsByid(
            @PathVariable UUID id
    ) {
        service.deleteLocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<Void> enableLocationsByid(
            @PathVariable UUID id
    ) {
        service.enableLocationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<LocationResponse> updateLocationsByid(
            @PathVariable UUID id,
            @RequestBody LocationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateLocation(id, request));
    }

    @PostMapping("/upload")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<List<LocationResponse>> uploadLocation(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.uploadLocation(file));
    }


}
