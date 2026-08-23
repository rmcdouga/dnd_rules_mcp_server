# Phase 1 File-by-File Worklist: Spring AI MCP Foundation

## Scope

This worklist covers **Phase 1 only** for the `dnd_rules_mcp_server` project: bootstrap the Spring AI MCP server foundation, application configuration, and basic auth/session health wiring.

The goal is to create the minimum set of Java classes needed to:

- start the server cleanly
- configure D&D Beyond connection settings
- support MCP server wiring
- provide a basic auth/session health check path
- create a foundation that later phases can build on

## Existing files to keep

These files already exist and should remain the starting point for Phase 1:

- `src/main/java/io/github/rmcdouga/dndrulesmcpserver/DndRulesMcpServerApplication.java`
- `src/test/java/io/github/rmcdouga/dndrulesmcpserver/DndRulesMcpServerApplicationTests.java`
- `src/main/resources/application.properties`

## Exact Java classes to create

### 1) `io.github.rmcdouga.dndrulesmcpserver.config.DdbProperties`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/config/DdbProperties.java`

**Purpose:**
Holds externalized D&D Beyond settings.

**Expected fields:**
- `baseUrl`
- `sessionCookiePath`
- `requestTimeout`
- `maxRetries`
- `cacheTtl`

**Notes:**
- Bind from `application.properties` / environment variables.
- Use a Spring configuration-properties pattern.

---

### 2) `io.github.rmcdouga.dndrulesmcpserver.config.McpServerProperties`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/config/McpServerProperties.java`

**Purpose:**
Holds MCP server-specific settings.

**Expected fields:**
- `serverName`
- `serverVersion`
- `transport`
- `enabled`

**Notes:**
- Keep this small and focused on MCP bootstrap concerns.
- This class should support future tool/prompt/resource registration flags if needed.

---

### 3) `io.github.rmcdouga.dndrulesmcpserver.config.HttpClientConfig`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/config/HttpClientConfig.java`

**Purpose:**
Defines shared HTTP client infrastructure for all outbound D&D Beyond requests.

**Expected beans:**
- `RestClient` or `RestTemplate` equivalent used by the D&D Beyond client layer
- timeout configuration support
- request interceptor support for auth/session propagation

**Notes:**
- Keep this class as the single place where transport-level client behavior is configured.
- Later phases can inject the client into API services.

---

### 4) `io.github.rmcdouga.dndrulesmcpserver.config.McpServerConfig`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/config/McpServerConfig.java`

**Purpose:**
Provides MCP server wiring and startup configuration.

**Expected responsibilities:**
- register the MCP server bean or equivalent Spring AI MCP configuration
- register future tool/prompt/resource components
- expose any server-level toggles needed for Phase 1 bootstrapping

**Notes:**
- This should stay focused on server bootstrap and not contain business logic.
- Later phases will add tool and prompt beans to the same overall MCP wiring pattern.

---

### 5) `io.github.rmcdouga.dndrulesmcpserver.health.DdbAuthHealthIndicator`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/health/DdbAuthHealthIndicator.java`

**Purpose:**
Reports whether the D&D Beyond session/authentication state looks valid.

**Expected behavior:**
- check for missing auth/session data
- report healthy vs unhealthy state
- avoid performing expensive calls unless needed

**Notes:**
- This can be a simple Spring Boot health indicator or a small service class if health endpoints are not yet wired.
- The Phase 1 goal is a basic health/auth check path, not full auth flows.

---

### 6) `io.github.rmcdouga.dndrulesmcpserver.service.AuthService`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/service/AuthService.java`

**Purpose:**
Encapsulates auth/session validation behavior for D&D Beyond.

**Expected methods:**
- `isAuthenticated()`
- `loadSession()`
- `validateSession()`

**Notes:**
- Keep the first version narrow.
- Later phases can expand this service to support setup/auth flows and authenticated client calls.

---

### 7) `io.github.rmcdouga.dndrulesmcpserver.api.DdbClient`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/api/DdbClient.java`

**Purpose:**
Provides the reusable D&D Beyond HTTP client abstraction.

**Phase 1 version should include:**
- base request methods
- session/auth header support
- error translation
- basic GET support for health validation

**Notes:**
- This is the client foundation for all later tool work.
- It does not need to support every endpoint in Phase 1, but the class should exist now.

---

### 8) `io.github.rmcdouga.dndrulesmcpserver.api.EndpointPaths`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/api/EndpointPaths.java`

**Purpose:**
Centralizes endpoint path constants and small URI builders.

**Expected contents:**
- path constants for D&D Beyond base routes
- simple builders for auth/session validation endpoints

**Notes:**
- This keeps literal endpoint strings out of services and controllers.
- Later phases can grow this into a fuller endpoint builder set.

---

### 9) `io.github.rmcdouga.dndrulesmcpserver.health.HealthStatusService`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/health/HealthStatusService.java`

**Purpose:**
Provides reusable health-check logic for MCP and application health reporting.

**Expected behavior:**
- combine auth state and client readiness into a simple status model
- supply a check that can be reused by a health indicator or a future MCP tool

**Notes:**
- This keeps health logic independent from transport wiring.

---

### 10) `io.github.rmcdouga.dndrulesmcpserver.model.HealthStatus`

**File:** `src/main/java/io/github/rmcdouga/dndrulesmcpserver/model/HealthStatus.java`

**Purpose:**
A small DTO/record for reporting health/auth status.

**Expected fields:**
- `healthy`
- `message`
- `details`

**Notes:**
- This can be a record if the project targets a modern enough Java version.
- Keep it minimal and easy to serialize.

---

## Phase 1 supporting test classes to create

### 11) `io.github.rmcdouga.dndrulesmcpserver.config.DdbPropertiesTests`

**File:** `src/test/java/io/github/rmcdouga/dndrulesmcpserver/config/DdbPropertiesTests.java`

**Purpose:**
Verifies configuration binding for D&D Beyond settings.

**Checks:**
- properties bind from `application.properties`
- defaults are applied correctly
- overrides can be provided via test properties

---

### 12) `io.github.rmcdouga.dndrulesmcpserver.config.McpServerConfigTests`

**File:** `src/test/java/io/github/rmcdouga/dndrulesmcpserver/config/McpServerConfigTests.java`

**Purpose:**
Verifies the MCP server configuration loads successfully.

**Checks:**
- Spring context loads with MCP config enabled
- required beans are present

---

### 13) `io.github.rmcdouga.dndrulesmcpserver.health.AuthHealthIndicatorTests`

**File:** `src/test/java/io/github/rmcdouga/dndrulesmcpserver/health/AuthHealthIndicatorTests.java`

**Purpose:**
Verifies the health/auth check logic.

**Checks:**
- healthy session scenario
- missing/invalid session scenario
- error reporting is readable

---

## Suggested implementation order

1. `config/DdbProperties`
2. `config/McpServerProperties`
3. `config/HttpClientConfig`
4. `api/EndpointPaths`
5. `api/DdbClient`
6. `service/AuthService`
7. `health/HealthStatus`
8. `health/HealthStatusService`
9. `health/DdbAuthHealthIndicator`
10. `config/McpServerConfig`
11. corresponding tests

## Phase 1 completion checklist

- [ ] The application boots as a Spring Boot MCP server.
- [ ] D&D Beyond config is externalized.
- [ ] A reusable client foundation exists.
- [ ] A basic auth/session health path exists.
- [ ] Phase 1 classes are test-covered and ready for Phase 2 endpoint work.
