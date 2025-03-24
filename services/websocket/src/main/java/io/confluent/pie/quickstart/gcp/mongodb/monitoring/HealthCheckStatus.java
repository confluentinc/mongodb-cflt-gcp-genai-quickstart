package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HealthCheckStatus {
    private String status;
    private List<HealthCheckStatusDetails> details;
}
