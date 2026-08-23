package io.github.rmcdouga.dndrulesmcpserver.config;

import org.springframework.context.annotation.Configuration;

/**
 * MCP server wiring and startup configuration.
 *
 * <p>Phase 1 keeps this minimal: the Spring AI MCP server auto-configuration
 * from {@code spring-ai-starter-mcp-server} handles most of the wiring.
 * This class exists as the central place to add future tool, prompt, and
 * resource registration beans.</p>
 *
 * <p>Later phases will add:</p>
 * <ul>
 *    <li>Tool callback beans for reference, character, and campaign tools</li>
 *    <li>Prompt provider beans for workflow prompts</li>
 *    <li>Resource provider beans for structured data access</li>
 * </ul>
 */
@Configuration
public class McpServerConfig {

      // Phase 1: no additional beans needed.
      // The spring-ai-starter-mcp-server auto-configuration provides the
      // MCP server infrastructure. Tools, prompts, and resources will be
      // registered in later phases via @McpTool, @McpPrompt, and @McpResource
      // annotations on Spring beans.
}