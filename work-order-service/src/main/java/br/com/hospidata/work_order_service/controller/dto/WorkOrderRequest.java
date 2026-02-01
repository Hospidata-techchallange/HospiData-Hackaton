package br.com.hospidata.work_order_service.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class WorkOrderRequest {
    private Long userId;
    private Integer priority;
    private String description;
    private List<WorkOrderItemRequest> items;
}