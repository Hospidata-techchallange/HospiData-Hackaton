package br.com.hospidata.stock_mcp_service.tool;

import br.com.hospidata.stock_mcp_service.dto.BatchDto;
import br.com.hospidata.stock_mcp_service.dto.ProductDto;
import br.com.hospidata.stock_mcp_service.dto.ResponseListDto;
import br.com.hospidata.stock_mcp_service.service.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchTool {

    private static final Logger log = LoggerFactory.getLogger(BatchTool.class);
    private final BatchService batchService;

    public BatchTool(BatchService batchService) {
        this.batchService = batchService;
    }

    @Tool(
            name = "findAllBatches",
            description = """
        Busca lotes de produtos no estoque hospitalar.

        Os resultados são ordenados automaticamente pela data de validade mais próxima
        (princípio FEFO - First Expire First Out).

        Filtros disponíveis:

        - productIds (List<String>, opcional):
          retorna lotes de múltiplos produtos.

        IMPORTANTE:
        Sempre que precisar buscar lotes de mais de um produto,
        utilize este campo em vez de chamar a ferramenta várias vezes.

        Exemplo:
        productIds = ["id1", "id2", "id3"]

        - batchNumber (String, opcional):
          busca um lote específico.

        - aisle (String, opcional):
          filtra por corredor do estoque.

        - active (Boolean, opcional):
          retorna apenas lotes ativos ou inativos.

        - nearExpirationDays (Integer, opcional):
          retorna lotes que vencem dentro de X dias.

        - expired (Boolean, opcional):
          quando true, retorna apenas lotes vencidos.
        """
    )
    public ResponseListDto<BatchTool> findAllBatches(
            List<String> productIds ,
            String batchNumber ,
            String aisle ,
            Boolean active ,
            Integer nearExpirationDays ,
            Boolean expired
    ){
        batchNumber = (batchNumber != null && !batchNumber.isBlank()) ? batchNumber : null;
        aisle = (aisle != null && !aisle.isBlank()) ? aisle : null;

        productIds = (productIds != null && !productIds.isEmpty()) ? productIds : null;

        nearExpirationDays = (nearExpirationDays != null && nearExpirationDays > 0)
                ? nearExpirationDays
                : null;

        long start = System.currentTimeMillis();
        log.info("Starting findAllBatches tool");

        try {
            List<BatchDto> batches = batchService.findAllBatches(
                    productIds,batchNumber,aisle,active,nearExpirationDays,expired
            );

            long duration = System.currentTimeMillis() - start;

            log.info(
                    "Finished findAllBatches - {} records found in {} ms",
                    batches.size(),
                    duration
            );

            return new ResponseListDto(batches.size(), batches);

        } catch (Exception ex) {

            long duration = System.currentTimeMillis() - start;

            log.error(
                    "Error in findAllBatches after {} ms",
                    duration,
                    ex
            );

            throw ex;

        }

    }


}
