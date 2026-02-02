package br.com.hospidata.work_order_service.controller.dto;

import lombok.Data;

@Data
public class WorkOrderItemRequest {
    private Long productId;
    private Integer quantity;
}