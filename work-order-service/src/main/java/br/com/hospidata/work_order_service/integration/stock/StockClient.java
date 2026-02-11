package br.com.hospidata.work_order_service.integration.stock;

import br.com.hospidata.work_order_service.config.FeignClientConfig; // Importe a config nova
import br.com.hospidata.work_order_service.integration.stock.dto.BatchDTO;
import br.com.hospidata.work_order_service.integration.stock.dto.StockReductionDTO;
import br.com.hospidata.work_order_service.integration.stock.dto.StockRestoreDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "stock-service", configuration = FeignClientConfig.class)
public interface StockClient {

    @GetMapping("/api/v1/stock/batch/product/{productId}/available")
    List<BatchDTO> getAvailableBatches(@PathVariable("productId") UUID productId);

    @PostMapping("/api/v1/stock/batch/reduce")
    void reduceStock(@RequestBody List<StockReductionDTO> requests);

    @PostMapping("/api/v1/stock/batch/restore")
    void restoreStock(@RequestBody List<StockRestoreDTO> requests);
}