package br.com.hospidata.stock_mcp_service.service;

import br.com.hospidata.stock_mcp_service.dto.BatchDto;
import br.com.hospidata.stock_mcp_service.mapper.BatchMapper;
import br.com.hospidata.stock_mcp_service.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BatchService {

    private final BatchMapper batchMapper;

    public BatchService(BatchMapper batchMapper) {
        this.batchMapper = batchMapper;
    }

    @Transactional(readOnly = true)
    public List<BatchDto> findAllBatches(
            List<String> productIds ,
            String batchNumber ,
            String aisle ,
            Boolean active ,
            Integer nearExpirationDays ,
            Boolean expired
    ){
        return batchMapper.findAllBatches(
                productIds,batchNumber,aisle,active,nearExpirationDays,expired
        );
    }

}
