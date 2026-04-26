package br.com.hospidata.appointment_mcp_service.service;

import br.com.hospidata.appointment_mcp_service.client.CategoryClient;
import br.com.hospidata.appointment_mcp_service.dto.category.CategoryClientResponse;
import br.com.hospidata.appointment_mcp_service.dto.category.CategoryResponse;
import br.com.hospidata.appointment_mcp_service.mapper.CategoryMappper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryClient client;
    private final CategoryMappper mapper;

    public CategoryService(CategoryClient client, CategoryMappper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public List<CategoryResponse> getAllCategories() {
        return mapper.toResponses(client.getAllCategories());
    }



}
