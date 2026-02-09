package br.com.hospidata.work_order_service.integration.stock.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockReductionDTO {
    private UUID batchId;
    private Integer quantity;
}