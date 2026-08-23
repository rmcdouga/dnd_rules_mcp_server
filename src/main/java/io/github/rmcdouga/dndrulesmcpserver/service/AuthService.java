package io.github.rmcdouga.dndrulesmcpserver.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.rmcdouga.dndrulesmcpserver.api.DdbClient;
import io.github.rmcdouga.dndrulesmcpserver.api.EndpointPaths;
import io.github.rmcdouga.dndrulesmcpserver.config.DdbProperties;

/**
 * Encapsulates auth/session validation behavior for D&D Beyond.
 *
 * <p>Phase 1 provides a narrow implementation that checks for the presence
 * of a session cookie file and optionally validates it via the API.
 * Later phases will expand this to support setup/auth flows and
 * authenticated client calls.</p>
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final DdbClient client;
    private final DdbProperties properties;

    public AuthService(DdbClient client, DdbProperties properties) {
        this.client = client;
        this.properties = properties;
     }

    /**
     * Check whether a session cookie file exists on disk.
     *
     * @return {@code true} if the session file is present
     */
    public boolean isAuthenticated() {
        Path sessionPath = Path.of(properties.sessionCookiePath());
        boolean exists = Files.exists(sessionPath);
        log.debug("Session file {} at {}", exists ? "found" : "not found", sessionPath);
        return exists;
     }

    /**
     * Load the session cookie from the configured file path.
     *
     * @return the cookie string, or {@code null} if the file does not exist
     */
    public String loadSession() {
        Path sessionPath = Path.of(properties.sessionCookiePath());
        if (!Files.exists(sessionPath)) {
            log.warn("Session file not found at {}", sessionPath);
            return null;
         }
        try {
            return Files.readString(sessionPath);
         } catch (Exception e) {
            log.error("Failed to read session file: {}", e.getMessage());
            return null;
         }
     }

    /**
     * Validate the current session by calling the D&D Beyond auth status endpoint.
     *
     * @return {@code true} if the session is valid, {@code false} otherwise
     */
    public boolean validateSession() {
        if (!isAuthenticated()) {
            return false;
         }
        try {
            String cookie = loadSession();
            Map<String, String> headers = Map.of("Cookie", "CobaltSession=" + cookie);
            Map<String, Object> response = client.get(EndpointPaths.AUTH_CHECK, headers);
            log.debug("Auth check response: {}", response);
            return response != null;
         } catch (Exception e) {
            log.warn("Session validation failed: {}", e.getMessage());
            return false;
         }
     }
}
