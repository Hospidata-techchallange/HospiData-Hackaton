package br.com.hospidata.work_order_service.controller;

import br.com.hospidata.work_order_service.controller.dto.WorkOrderRequest;
import br.com.hospidata.work_order_service.controller.dto.WorkOrderResponse;
import br.com.hospidata.work_order_service.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService service;

    @PostMapping
    public ResponseEntity<WorkOrderResponse> createWorkOrder(@RequestBody WorkOrderRequest request) {
        WorkOrderResponse response = service.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponse> getWorkOrder(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }
}