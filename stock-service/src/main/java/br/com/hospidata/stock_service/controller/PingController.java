package br.com.hospidata.stock_service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stocks")
public class PingController {

    @GetMapping
    public String ping() {
        return "pong";
    }

}
