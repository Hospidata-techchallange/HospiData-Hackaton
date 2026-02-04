package br.com.hospidata.stock_service.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 100, message = "Product name must have at most 100 characters")
        String name,

        @Size(max = 255, message = "Description must have at most 255 characters")
        String description,

        @NotBlank(message = "SKU code is required")
        @Size(max = 50, message = "SKU code must have at most 50 characters")
        String skuCode,

        @NotNull(message = "Category id is required") UUID categoryId,

        @NotNull(message = "Minimum stock alert is required")
        @Min(value = 0, message = "Minimum stock alert cannot be negative")
        Integer minStockAlert

) {

}
