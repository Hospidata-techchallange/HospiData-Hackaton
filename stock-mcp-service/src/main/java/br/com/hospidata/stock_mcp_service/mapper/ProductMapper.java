package br.com.hospidata.stock_mcp_service.mapper;

import br.com.hospidata.stock_mcp_service.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ProductMapper {

    List<ProductDto> findAllProducts(
            @Param("name") String name,
            @Param("skuCode") String skuCode,
            @Param("categoryId") String categoryId,
            @Param("active") Boolean active
    );

}
