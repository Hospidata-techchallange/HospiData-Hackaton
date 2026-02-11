package br.com.hospidata.work_order_service.integration.stock.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class StockRestoreDTO {
    private UUID productId;
    private Integer quantity;
}