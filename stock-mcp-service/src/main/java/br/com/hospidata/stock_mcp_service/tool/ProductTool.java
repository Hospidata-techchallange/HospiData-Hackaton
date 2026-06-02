package br.com.hospidata.stock_mcp_service.tool;

import br.com.hospidata.stock_mcp_service.dto.ProductDto;
import br.com.hospidata.stock_mcp_service.dto.ResponseListDto;
import br.com.hospidata.stock_mcp_service.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductTool {

    private static final Logger log = LoggerFactory.getLogger(ProductTool.class);
    private final ProductService productService;

    public ProductTool(ProductService productService) {
        this.productService = productService;
    }

    @Tool(
            name = "findAllProducts",
            description = """
        Busca produtos hospitalares e medicamentos cadastrados no estoque.
    
        Filtros disponíveis:
        - name (str, opcional): busca produtos que contenham o texto informado no nome.
        - sku_code (str, opcional): busca um produto pelo código SKU.
        - category_id (str, opcional): retorna produtos pertencentes a uma categoria específica.
        - active (bool, opcional): filtra produtos ativos ou inativos.
    
        Use esta ferramenta para localizar produtos no estoque.
        """
    )
    public ResponseListDto<ProductDto> findAllProducts(
            String name ,
            String skuCode ,
            String categoryId ,
            Boolean active
    ) {

        name = (name != null && !name.isBlank()) ? name : null;
        skuCode = (skuCode != null && !skuCode.isBlank()) ? skuCode : null;
        categoryId = (categoryId != null && !categoryId.isBlank()) ? categoryId : null;

        long start = System.currentTimeMillis();
        log.info("Starting findAllProducts tool");

        log.info(
                "findAllProducts called with name='{}', skuCode='{}', categoryId='{}', active={}",
                name, skuCode, categoryId, active
        );

        try {
            List<ProductDto> products =  productService.findAllProducts(name, skuCode, categoryId, active);

            long duration = System.currentTimeMillis() - start;

            log.info(
                    "Finished findAllProducts - {} records found in {} ms",
                    products.size(),
                    duration
            );

            return new ResponseListDto(products.size(), products);

        } catch (Exception ex) {

            long duration = System.currentTimeMillis() - start;

            log.error(
                    "Error in findAllProducts after {} ms",
                    duration,
                    ex
            );

            throw ex;

        }


    }

}
