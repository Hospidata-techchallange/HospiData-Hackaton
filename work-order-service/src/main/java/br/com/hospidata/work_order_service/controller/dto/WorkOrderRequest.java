package br.com.hospidata.work_order_service.controller.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class WorkOrderRequest {
    private UUID userId;
    private Integer priority;
    private String description;
    private List<WorkOrderItemRequest> items;
}