package io.github.rmcdouga.dndrulesmcpserver.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.rmcdouga.dndrulesmcpserver.config.DdbProperties;

/**
 * Reusable D&D Beyond HTTP client abstraction.
 *
 * <p>Phase 1 provides the foundation: base request methods, session/auth
 * header support, and basic GET support for health validation. Later phases
 * will add typed endpoint methods for characters, campaigns, and reference data.</p>
 */
@Component
public class DdbClient {

    private static final Logger log = LoggerFactory.getLogger(DdbClient.class);

    private final RestClient restClient;
    private final DdbProperties properties;

    public DdbClient(RestClient restClient, DdbProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
     }

    /**
     * Perform a GET request to the given endpoint path.
     *
     * @param path     the endpoint path (e.g. {@code /api/characters})
     * @param headers   additional request headers (may be null)
     * @return the response body as a generic map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String path, Map<String, String> headers) {
        log.debug("GET {}{}", properties.baseUrl(), path);

        RestClient.RequestHeadersSpec<?> spec = restClient.get().uri(path);

        if (headers != null) {
            headers.forEach(spec::header);
         }

        return spec.retrieve().body(Map.class);
     }

    /**
     * Perform a GET request with no extra headers.
     */
    public Map<String, Object> get(String path) {
        return get(path, null);
     }

    /**
     * Perform a GET request and return the raw response body as a string.
     */
    public String getRaw(String path, Map<String, String> headers) {
        log.debug("GET (raw) {}{}", properties.baseUrl(), path);

        RestClient.RequestHeadersSpec<?> spec = restClient.get().uri(path);

        if (headers != null) {
            headers.forEach(spec::header);
         }

        return spec.retrieve().body(String.class);
     }

    /**
     * Perform a GET request and return the raw response body as a string.
     */
    public String getRaw(String path) {
        return getRaw(path, null);
     }
}