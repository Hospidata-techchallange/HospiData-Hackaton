package br.com.hospidata.stock_service.mapper;

import br.com.hospidata.stock_service.controller.dto.LocationRequest;
import br.com.hospidata.stock_service.controller.dto.LocationResponse;
import br.com.hospidata.stock_service.entity.Location;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class LocationMapper {

    public Location toEntity(LocationRequest dto) {
        return Location.builder()
                .aisle(dto.aisle())
                .shelf(dto.shelf())
                .bin(dto.bin())
                .description(dto.description())
                .build();
    }

    public List<Location> toEntities(List<LocationRequest> dtos) {
        return dtos.stream()
                .map(dto -> toEntity(dto))
                .toList();
    }

    public LocationResponse toResponse(Location entity) {
        return new LocationResponse(
                entity.getId(),
                entity.getAisle(),
                entity.getShelf(),
                entity.getBin(),
                entity.getDescription(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getLastUpdatedAt()
        );
    }

    public List<LocationResponse> toResponses(List<Location> entities) {
        return entities.stream()
                .map(entity -> toResponse(entity))
                .toList();
    }

    public Location fromCsv(CSVRecord record) {
        Location location = new Location();
        location.setAisle(record.get("aisle"));
        location.setShelf(record.get("shelf"));
        location.setBin(record.get("bin"));
        location.setDescription(record.get("description"));
        return location;
    }


    public List<Location> fromCsv(CSVParser parser) {
        return StreamSupport.stream(parser.spliterator(), false)
                .map(this::fromCsv)
                .toList();
    }
}
