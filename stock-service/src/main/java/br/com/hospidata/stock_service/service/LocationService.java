package br.com.hospidata.stock_service.service;

import br.com.hospidata.common.exceptions.ListCannotEmpty;
import br.com.hospidata.common.exceptions.ResourceNotFoundException;
import br.com.hospidata.stock_service.controller.dto.LocationRequest;
import br.com.hospidata.stock_service.controller.dto.LocationResponse;
import br.com.hospidata.stock_service.entity.Location;
import br.com.hospidata.stock_service.entity.Product;
import br.com.hospidata.stock_service.mapper.LocationMapper;
import br.com.hospidata.stock_service.repository.LocationRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    LocationRepository repository;
    LocationMapper mapper;

    public LocationService(LocationRepository repository, LocationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public List<LocationResponse> createLocations(List<LocationRequest> request) {

        if (request == null ||  request.isEmpty()) {
            throw  new ListCannotEmpty();
        }

        return mapper.toResponses(repository.saveAll(mapper.toEntities(request)));
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> findAllLocations(Boolean active) {
        if (active == null) {
            return  mapper.toResponses(repository.findAll());
        }
        return mapper.toResponses(repository.findByActive(active));
    }

    @Transactional(readOnly = true)
    public  LocationResponse findLocationById(UUID id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Location" , "id", id.toString())));
    }

    @Transactional
    public void deleteLocation(UUID id) {
        repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Location" , "id", id.toString()));
        repository.deleteById(id);
    }

    @Transactional
    public void enableLocationById(UUID id) {
        Location location = repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Location" , "id" , id.toString()) );
        location.setActive(true);
        repository.save(location);
    }

    @Transactional
    public LocationResponse updateLocation(UUID id, LocationRequest request) {
        Location location = repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Location" , "id", id.toString()));

        location.setAisle(request.aisle());
        location.setShelf(request.shelf());
        location.setBin(request.bin());
        location.setDescription(request.description());

        return mapper.toResponse(repository.save(location));
    }

    @Transactional
    public List<LocationResponse> uploadLocation(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo CSV vazio");
        }

        try (
            Reader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
            ) {

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            List<Location> locations = mapper.fromCsv(parser);
            List<LocationResponse> saved = mapper.toResponses(repository.saveAll(locations));
            return saved;
        }catch (Exception e) {
            throw new RuntimeException("Erro ao processar o CSV", e);
        }
    }



}
