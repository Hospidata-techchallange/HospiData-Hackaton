package br.com.hospidata.work_order_service.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PickingInstructionDTO {
    private UUID productId;
    private UUID batchId;
    private Integer quantityToPick;
    private String location;
    private LocalDate expirationDate;
}