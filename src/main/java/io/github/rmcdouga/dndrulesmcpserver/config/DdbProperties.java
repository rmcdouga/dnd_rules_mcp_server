package io.github.rmcdouga.dndrulesmcpserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Externalized D&D Beyond connection settings.
 *
 * <p>Bound from {@code dndbeyond.*} properties in {@code application.properties}
 * or environment variables such as {@code DND_BEYOND_BASE_URL}.</p>
 */
@ConfigurationProperties(prefix = "dndbeyond")
public record DdbProperties(
        String baseUrl,
        String sessionCookiePath,
        int requestTimeoutSeconds,
        int maxRetries,
        int cacheTtlSeconds
) {
    public DdbProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://www.dndbeyond.com";
        }
        if (sessionCookiePath == null || sessionCookiePath.isBlank()) {
            sessionCookiePath = System.getProperty("user.home") + "/.dndbeyond-mcp/config.json";
        }
        if (requestTimeoutSeconds <= 0) {
            requestTimeoutSeconds = 30;
        }
        if (maxRetries < 0) {
            maxRetries = 3;
        }
        if (cacheTtlSeconds <= 0) {
            cacheTtlSeconds = 86_400; // 24 hours
        }
    }
}
