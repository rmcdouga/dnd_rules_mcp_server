package io.github.rmcdouga.dndrulesmcpserver.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.rmcdouga.dndrulesmcpserver.model.HealthStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the health/auth check logic.
 */
@SpringBootTest
class AuthHealthIndicatorTests {

    @Autowired
    private HealthStatusService healthStatusService;

    @Autowired
    private DdbAuthHealthIndicator healthIndicator;

    @Test
    void healthStatusServiceReturnsStatus() {
        HealthStatus status = healthStatusService.checkHealth();
        assertThat(status).isNotNull();
        assertThat(status.message()).isNotBlank();
      }

    @Test
    void healthIndicatorReturnsHealth() {
        var health = healthIndicator.health();
        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isNotNull();
      }

    @Test
    void healthStatusDetailsArePresent() {
        HealthStatus status = healthStatusService.checkHealth();
        assertThat(status.details()).isNotNull();
        assertThat(status.details()).containsKey("sessionFile");
      }
}
