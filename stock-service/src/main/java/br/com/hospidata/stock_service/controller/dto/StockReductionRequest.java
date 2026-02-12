package br.com.hospidata.stock_service.controller.dto;

import java.util.UUID;

public record StockReductionRequest(
        UUID batchId,
        Integer quantity
) {}