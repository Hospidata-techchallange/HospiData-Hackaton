package br.com.hospidata.stock_service.controller.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StockRestoreRequest {
    private UUID productId;
    private Integer quantity;
}