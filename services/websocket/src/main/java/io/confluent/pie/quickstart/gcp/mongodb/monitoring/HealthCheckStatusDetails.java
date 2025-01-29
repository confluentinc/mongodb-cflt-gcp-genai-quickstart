package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HealthCheckStatusDetails {
    private String status;
    private String name;
    private Date lastCheck;
    private String error;

    public HealthCheckStatusDetails(String status, String name, Date lastCheck) {
        this.status = status;
        this.name = name;
        this.lastCheck = lastCheck;
    }
}
