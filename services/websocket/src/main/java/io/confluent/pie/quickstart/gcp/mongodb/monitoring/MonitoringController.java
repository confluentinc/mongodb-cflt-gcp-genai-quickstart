package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/health")
public class MonitoringController {

    private final MonitoringConfig monitoringConfig;
    private final RestTemplate restTemplate;
    private final Map<String, HealthCheckStatusDetails> healthCheckStatusDetails = new ConcurrentHashMap<>();

    public MonitoringController(@Autowired MonitoringConfig monitoringConfig,
                                @Autowired RestTemplate restTemplate) {
        this.monitoringConfig = monitoringConfig;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 5000 * 60)
    public void onHealthCheck() {

        monitoringConfig.getServices().forEach(service -> {
            try {
                final ResponseEntity<MonitoredStatusResponse> response = restTemplate.getForEntity(
                        service + "/health",
                        MonitoredStatusResponse.class);
                log.info("Service {} is {}", service, Objects.requireNonNull(response.getBody()).getStatus());

                healthCheckStatusDetails.put(service, new HealthCheckStatusDetails(
                        response.getBody().getStatus(),
                        service,
                        new Date()));
            } catch (Exception e) {
                log.error("Service {} health check failed.", service, e);

                healthCheckStatusDetails.put(service, new HealthCheckStatusDetails(
                        "DOWN",
                        service,
                        new Date(),
                        e.getMessage()));
            }
        });
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<HealthCheckStatus> getHealth() {
        final Collection<HealthCheckStatusDetails> details = healthCheckStatusDetails.values();

        return ResponseEntity.ok(new HealthCheckStatus(
                "UP",
                new ArrayList<>(details)));
    }

}
