package br.com.hospidata.stock_mcp_service.mapper;

import br.com.hospidata.stock_mcp_service.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> findAllCategories(Boolean active);

}