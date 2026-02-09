package br.com.hospidata.work_order_service.controller.dto;

import br.com.hospidata.work_order_service.entity.enums.WorkOrderStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class WorkOrderResponse {
    private UUID id;
    private WorkOrderStatus status;
    private LocalDateTime createdAt;
    private BigDecimal totalOrderCost;
    private List<PickingInstructionDTO> pickingInstructions;
}