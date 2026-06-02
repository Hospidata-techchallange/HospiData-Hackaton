package br.com.hospidata.stock_mcp_service.mapper;

import br.com.hospidata.stock_mcp_service.dto.BatchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Array;

import java.util.List;

@Mapper
public interface BatchMapper {

    List<BatchDto> findAllBatches(
            @Param("productIds") List<String> productIds,
            @Param("batchNumber") String batchNumber,
            @Param("aisle") String aisle,
            @Param("active") Boolean active,
            @Param("nearExpirationDays") Integer nearExpirationDays,
            @Param("expired") Boolean expired
    );

}
