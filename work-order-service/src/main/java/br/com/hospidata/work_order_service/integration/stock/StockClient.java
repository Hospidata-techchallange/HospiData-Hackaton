package br.com.hospidata.work_order_service.integration.stock;

import br.com.hospidata.work_order_service.integration.stock.dto.BatchDTO;
import br.com.hospidata.work_order_service.integration.stock.dto.StockReductionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "stock-service")
public interface StockClient {

    @GetMapping("/api/batches/product/{productId}/available")
    List<BatchDTO> getAvailableBatches(@PathVariable("productId") UUID productId);

    @PostMapping("/batches/reduce")
    void reduceStock(@RequestBody List<StockReductionDTO> requests);

}