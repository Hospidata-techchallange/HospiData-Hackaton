package br.com.hospidata.stock_service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotBlank
        @Size(max = 10)
        String aisle,

        @NotBlank
        @Size(max = 10)
        String shelf,

        @NotBlank
        @Size(max = 10)
        String bin,

        @Size(max = 100)
        String description

) {
}
