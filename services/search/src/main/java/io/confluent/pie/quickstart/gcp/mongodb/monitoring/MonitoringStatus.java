package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringStatus {
    private String status;
    private HashMap<String, Object> details;

    public MonitoringStatus(String status) {
        this.status = status;
    }
}
