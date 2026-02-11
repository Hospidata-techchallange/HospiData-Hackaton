package br.com.hospidata.work_order_service.controller;

import br.com.hospidata.work_order_service.controller.dto.WorkOrderRequest;
import br.com.hospidata.work_order_service.controller.dto.WorkOrderResponse;
import br.com.hospidata.work_order_service.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/work-order")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService service;

    @PostMapping
    public ResponseEntity<WorkOrderResponse> createWorkOrder(@RequestBody WorkOrderRequest request) {
        WorkOrderResponse response = service.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponse> getWorkOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderResponse>> getAllWorkOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderResponse> updateWorkOrder(@PathVariable UUID id, @RequestBody WorkOrderRequest request) {
        return ResponseEntity.ok(service.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable UUID id) {
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}