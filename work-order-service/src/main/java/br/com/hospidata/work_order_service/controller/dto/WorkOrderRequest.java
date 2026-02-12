package br.com.hospidata.work_order_service.controller.dto;

import br.com.hospidata.work_order_service.entity.enums.WorkOrderPriority;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class WorkOrderRequest {
    private UUID userId;
    private WorkOrderPriority priority;
    private String description;
    private List<WorkOrderItemRequest> items;
}