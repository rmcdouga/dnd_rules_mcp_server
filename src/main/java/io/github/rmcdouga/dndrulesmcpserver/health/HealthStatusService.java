package io.github.rmcdouga.dndrulesmcpserver.health;

import java.util.Map;

import org.springframework.stereotype.Service;

import io.github.rmcdouga.dndrulesmcpserver.model.HealthStatus;
import io.github.rmcdouga.dndrulesmcpserver.service.AuthService;

/**
 * Provides reusable health-check logic for MCP and application health reporting.
 *
 * <p>Combines auth state and client readiness into a simple {@link HealthStatus}
 * model that can be consumed by a health indicator or a future MCP tool.</p>
 */
@Service
public class HealthStatusService {

    private final AuthService authService;

    public HealthStatusService(AuthService authService) {
        this.authService = authService;
      }

     /**
     * Check overall system health by combining auth state and basic readiness.
     *
     * @return a {@link HealthStatus} describing the current state
     */
    public HealthStatus checkHealth() {
        boolean authenticated = authService.isAuthenticated();

        if (authenticated) {
            return HealthStatus.ok("D&D Beyond session file present",
                    Map.of("sessionFile", "present"));
          }

        return HealthStatus.unhealthy("D&D Beyond session file missing",
                Map.of("sessionFile", "missing"));
      }
}
