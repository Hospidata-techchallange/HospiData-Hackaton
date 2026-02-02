package br.com.hospidata.work_order_service.integration.stock;

import br.com.hospidata.work_order_service.integration.stock.dto.BatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "stock-service")
public interface StockClient {

    @GetMapping("/api/batches/product/{productId}/available")
    List<BatchDTO> getAvailableBatches(@PathVariable("productId") Long productId);

}