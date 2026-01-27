package br.com.hospidata.auth._service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Pong {

    @Id
    private Long id;

    private String message;

    public Pong() {

    }

    public Pong(Long id, String message) {
        this.id = id;
        this.message = message;
    }

}
