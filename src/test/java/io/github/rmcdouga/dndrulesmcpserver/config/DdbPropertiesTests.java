package io.github.rmcdouga.dndrulesmcpserver.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that {@link DdbProperties} binds correctly from application properties.
 */
@SpringBootTest
class DdbPropertiesTests {

    @Autowired
    private DdbProperties properties;

    @Test
    void defaultBaseUrlIsSet() {
        assertThat(properties.baseUrl()).isEqualTo("https://www.dndbeyond.com");
      }

    @Test
    void defaultSessionCookiePathIsSet() {
        assertThat(properties.sessionCookiePath()).isNotBlank();
        assertThat(properties.sessionCookiePath()).contains(".dndbeyond-mcp");
      }

    @Test
    void defaultTimeoutIsPositive() {
        assertThat(properties.requestTimeoutSeconds()).isGreaterThan(0);
      }

    @Test
    void defaultMaxRetriesIsNonNegative() {
        assertThat(properties.maxRetries()).isGreaterThanOrEqualTo(0);
      }

    @Test
    void defaultCacheTtlIsPositive() {
        assertThat(properties.cacheTtlSeconds()).isGreaterThan(0);
      }
}
