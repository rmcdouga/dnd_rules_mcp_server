package io.github.rmcdouga.dndrulesmcpserver.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.health.contributor.Status;
import org.springframework.stereotype.Component;

/**
 * Spring Boot health indicator that reports D&D Beyond authentication status.
 *
 * <p>Reports healthy when the session cookie file is present, unhealthy otherwise.
 * This is a lightweight check that avoids expensive API calls.</p>
 */
@Component("ddbAuth")
public class DdbAuthHealthIndicator implements HealthIndicator {

    private final HealthStatusService healthStatusService;

    public DdbAuthHealthIndicator(HealthStatusService healthStatusService) {
        this.healthStatusService = healthStatusService;
      }

     @Override
    public Health health() {
        var status = healthStatusService.checkHealth();

        if (status.healthy()) {
            return Health.up()
                    .withDetail("message", status.message())
                    .withDetails(status.details())
                    .build();
          }

        return Health.down()
                .withDetail("message", status.message())
                .withDetails(status.details())
                .build();
      }
}