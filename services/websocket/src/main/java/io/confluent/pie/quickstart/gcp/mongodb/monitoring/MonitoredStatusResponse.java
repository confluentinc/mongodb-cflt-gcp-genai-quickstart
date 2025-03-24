package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Monitoring status response.
 * Contains the status of the monitored service and details about the services being monitored.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonitoredStatusResponse {
    private String status;
    private Map<String, Object> details;
}
