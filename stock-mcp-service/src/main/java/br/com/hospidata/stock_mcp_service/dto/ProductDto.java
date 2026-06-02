package br.com.hospidata.stock_mcp_service.dto;

import java.util.UUID;

public record ProductDto(

        UUID idProduct ,
        String name ,
        String description ,
        String skuCode ,
        Integer minStockAlert ,
        UUID categoryId ,
        String categoryName ,
        Boolean active

) {

}
