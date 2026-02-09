package br.com.hospidata.work_order_service.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderItemRequest {

    private UUID productId;
    private Integer quantity;

}