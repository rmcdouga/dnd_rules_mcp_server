package io.github.rmcdouga.dndrulesmcpserver.model;

import java.util.Map;

/**
 * Simple DTO for reporting health and authentication status.
 *
 * @param healthy  whether the system is in a healthy state
 * @param message  human-readable status message
 * @param details  optional key-value details for diagnostics
 */
public record HealthStatus(
        boolean healthy,
        String message,
        Map<String, Object> details
) {
    public static HealthStatus ok(String message) {
        return new HealthStatus(true, message, Map.of());
    }

    public static HealthStatus ok(String message, Map<String, Object> details) {
        return new HealthStatus(true, message, details);
    }

    public static HealthStatus unhealthy(String message) {
        return new HealthStatus(false, message, Map.of());
    }

    public static HealthStatus unhealthy(String message, Map<String, Object> details) {
        return new HealthStatus(false, message, details);
    }
}
