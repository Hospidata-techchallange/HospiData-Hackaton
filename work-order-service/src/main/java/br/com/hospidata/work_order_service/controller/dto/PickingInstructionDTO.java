package br.com.hospidata.work_order_service.controller.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class PickingInstructionDTO {
    private Long productId;
    private Long batchId;
    private Integer quantityToPick;
    private String location;
    private LocalDate expirationDate;
}