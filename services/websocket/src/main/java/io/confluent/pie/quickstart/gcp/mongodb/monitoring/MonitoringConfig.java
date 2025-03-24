package io.confluent.pie.quickstart.gcp.mongodb.monitoring;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration("monitoring")
@EnableScheduling
@Getter
public class MonitoringConfig {

    @Value("#{'${monitoring.services}'.split(',')}")
    private List<String> services;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
