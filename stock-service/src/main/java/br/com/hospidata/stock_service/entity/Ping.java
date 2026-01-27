package br.com.hospidata.stock_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Ping {

    @Id
    private Long id;

    private String message;

    public Ping() {}

    public Ping(Long id, String message) {
        this.id = id;
        this.message = message;
    }

}
