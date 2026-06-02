package br.com.hospidata.stock_mcp_service.service;

import br.com.hospidata.stock_mcp_service.dto.ProductDto;
import br.com.hospidata.stock_mcp_service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public  ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findAllProducts(
            String name ,
            String skuCode ,
            String categoryId ,
            Boolean active
    ) {
        return productMapper.findAllProducts(
                name , skuCode , categoryId , active
        );
    }

}
