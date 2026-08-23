package io.github.rmcdouga.dndrulesmcpserver.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Verifies that the MCP server configuration loads successfully.
 */
@SpringBootTest
class McpServerConfigTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void mcpServerConfigBeanExists() {
        assertThatNoException().isThrownBy(() -> context.getBean(McpServerConfig.class));
    }

    @Test
    void contextLoads() {
        // If we get here, the Spring context loaded successfully
        assertThat(context).isNotNull();
    }
}