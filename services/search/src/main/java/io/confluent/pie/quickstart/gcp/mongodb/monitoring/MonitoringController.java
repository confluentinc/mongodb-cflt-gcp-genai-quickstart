package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/health")
public class MonitoringController {

    @GetMapping(produces = "application/json")
    public ResponseEntity<MonitoringStatus> getHealth() {
        return ResponseEntity.ok(new MonitoringStatus("UP"));
    }

}
