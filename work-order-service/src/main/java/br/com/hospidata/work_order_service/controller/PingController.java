package br.com.hospidata.work_order_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/work-order")
public class PingController {

    @GetMapping
    public String ping() {
        return "pong";
    }

}
