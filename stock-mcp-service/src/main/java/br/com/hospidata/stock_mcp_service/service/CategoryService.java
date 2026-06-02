package br.com.hospidata.stock_mcp_service.service;

import br.com.hospidata.stock_mcp_service.dto.CategoryDto;
import br.com.hospidata.stock_mcp_service.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    @Transactional(readOnly = true)
    public List<CategoryDto> findAllCategories(Boolean active) {
        return categoryMapper.findAllCategories(active);
    }
}
