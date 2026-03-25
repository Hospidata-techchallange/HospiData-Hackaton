package br.com.hospidata.stock_mcp_service.dto;

import java.util.List;

public record ResponseListDto<T>(
        int count,
        List<T> data
) {

}
