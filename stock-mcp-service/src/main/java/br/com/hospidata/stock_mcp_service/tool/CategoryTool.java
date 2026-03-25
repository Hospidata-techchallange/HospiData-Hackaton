package br.com.hospidata.stock_mcp_service.tool;

import br.com.hospidata.stock_mcp_service.dto.CategoryDto;
import br.com.hospidata.stock_mcp_service.dto.ResponseListDto;
import br.com.hospidata.stock_mcp_service.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryTool {

    private static final Logger log = LoggerFactory.getLogger(CategoryTool.class);
    private final CategoryService categoryService;

    public CategoryTool(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Tool(
        name = "findAllCategories",
        description = """
    Busca categorias de medicamentos e materiais hospitalares no estoque.

    Parâmetros:
    - active (bool, opcional): filtra categorias ativas ou inativas.
    """
    )
    public ResponseListDto<CategoryDto> findAllCategories(Boolean active) {

        long start = System.currentTimeMillis();
        log.info("Starting findAllCategories tool");

        try {
            List<CategoryDto> categories = categoryService.findAllCategories(active);

            long duration = System.currentTimeMillis() - start;

            log.info(
                    "Finished findAllCategories - {} records found in {} ms",
                    categories.size(),
                    duration
            );

            return new ResponseListDto(categories.size(), categories);

        } catch (Exception ex) {

            long duration = System.currentTimeMillis() - start;

            log.error(
                    "Error in findAllCategories after {} ms",
                    duration,
                    ex
            );

            throw ex;
        }
    }

}
