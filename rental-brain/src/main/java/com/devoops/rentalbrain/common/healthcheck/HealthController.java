package com.devoops.rentalbrain.common.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthController {
    @GetMapping("/health")
    public String healthCheck() {
        log.info("health check");
        return "I'm OK";
    }

    @GetMapping("/")
    public String health() {
        log.info("health");
        return "I'm OK";
    }
}
