package br.com.hospidata.stock_service.controller;

import br.com.hospidata.common_security.aspect.CheckRole;
import br.com.hospidata.common_security.dto.MeResponse;
import br.com.hospidata.common_security.enums.Role;
import br.com.hospidata.common_security.service.TokenService;
import br.com.hospidata.stock_service.controller.dto.*;
import br.com.hospidata.stock_service.service.BatchService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock/batch")
public class BatchController {

    private final BatchService service;
    private final TokenService tokenService;

    public BatchController(BatchService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<List<BatchResponse>> addBatch(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            @RequestBody List<BatchRequest> requests
    ) {
        MeResponse user = tokenService.getUserInformation(accessToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBatches(user.email(), requests));
    }

    @GetMapping
    @CheckRole({Role.NURSE , Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<List<BatchResponse>> getAllBatches(
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllBatches(active));
    }

    @GetMapping("/filter")
    @CheckRole({Role.NURSE, Role.PHARMACIST, Role.ADMIN})
    public ResponseEntity<Page<BatchResponse>> getAllBatches(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.filterBatches(search, pageable));
    }

    @GetMapping("/{id}")
    @CheckRole({Role.NURSE , Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<BatchResponse> getBatchById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findBatchById(id));
    }

    @DeleteMapping("/{id}")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<Void> deleteBatchById(
            @PathVariable UUID id,
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        MeResponse user = tokenService.getUserInformation(accessToken);
        service.deleteBatchById(id , user.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/enable/{id}")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<Void> enableBatchById(
            @PathVariable UUID id,
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        MeResponse user = tokenService.getUserInformation(accessToken);
        service.enableBatchById(id , user.email());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<BatchResponse> updateBatchById(
            @RequestBody BatchRequestUpdate request,
            @PathVariable UUID id,
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        MeResponse user = tokenService.getUserInformation(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(service.updateBatchById(id , request , user.email()));
    }

    @PostMapping("/upload")
    @CheckRole({Role.PHARMACIST , Role.ADMIN})
    public ResponseEntity<List<BatchResponse>> uploadBatch(
            @RequestParam("file") MultipartFile file ,
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        MeResponse user = tokenService.getUserInformation(accessToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.uploadBatches(file, user.email()))  ;
    }

    @PostMapping("/reduce")
    public ResponseEntity<Void> reduceStock(@RequestBody List<StockReductionRequest> requests) {
        service.reduceStock(requests, "SYSTEM_WORK_ORDER");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/restore")
    @CheckRole({Role.NURSE, Role.PHARMACIST, Role.ADMIN}) // ADICIONADO
    public ResponseEntity<Void> restoreStock(@RequestBody List<StockRestoreRequest> requests) {
        service.restoreStock(requests, "SYSTEM_WORK_ORDER_RESTORE");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}/available")
    @CheckRole({Role.NURSE, Role.PHARMACIST, Role.ADMIN})
    public ResponseEntity<List<BatchResponse>> getAvailableBatches(@PathVariable UUID productId) {
        return ResponseEntity.ok(service.findBatchesByProductId(productId));
    }

}
