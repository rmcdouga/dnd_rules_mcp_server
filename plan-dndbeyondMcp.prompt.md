# Phase-by-phase implementation checklist for the `dndbeyond-mcp` migration in `dnd_rules_mcp_server`

## Overall objective

Rebuild the `dndbeyond-mcp` feature set inside the `dnd_rules_mcp_server` project as a Java Spring Boot + Spring AI MCP server, preserving the existing MCP-facing tool, prompt, and resource contract as closely as practical. This plan intentionally ignores the separate `FGU_MCP_Server` project.

## Guiding principles

- [ ] Keep the MCP surface familiar to existing clients.
- [ ] Prefer a clean Spring Boot architecture with clear separation between MCP entrypoints, API clients, and domain mapping.
- [ ] Reuse the strongest ideas from `dndbeyond-mcp`: prompt-based workflows, tool-first access, structured resources, and cached reference data.
- [ ] Treat the D&D Beyond API as the authoritative source for account-specific and character-specific data.
- [ ] Reimplement behavior idiomatically in Spring rather than porting line by line.

## Phase 0: Contract baseline

- [x] Inventory the current TypeScript MCP contract in `dndbeyond-mcp`.
- [x] Record all tool names, parameter shapes, and return expectations.
- [x] Record all prompt names and their workflow intent.
- [x] Record all resource URIs and the payload shape each should expose.
- [x] Identify every upstream D&D Beyond endpoint used by the current server.
- [x] Document which operations are read-only, write/mutation, or workflow-only.

Captured in `docs/dndbeyond-mcp-contract.md`.

### Exit criteria

- [x] A migration reference doc exists that lists the full MCP surface area.
- [x] Tool/prompt/resource parity requirements are clear.

## Phase 1: Spring Boot and MCP foundation

- [ ] Add or confirm Spring AI MCP server support in `dnd_rules_mcp_server`.
- [ ] Establish the application entrypoint and base package structure.
- [ ] Define MCP server configuration and startup wiring.
- [ ] Add configuration properties for:
  - [ ] D&D Beyond base URLs
  - [ ] auth/session storage
  - [ ] cache settings
  - [ ] timeouts/retry policy
- [ ] Implement a basic auth/session health-check path.
- [ ] Add minimal startup tests to prove the app boots as an MCP server.

### Exit criteria

- [ ] The Java app starts successfully as a Spring Boot MCP server.
- [ ] Core configuration is externally configurable.

## Phase 2: D&D Beyond client foundation

- [ ] Build a typed D&D Beyond REST client equivalent to `DdbClient`.
- [ ] Add endpoint builders for:
  - [ ] character routes
  - [ ] campaign routes
  - [ ] reference routes
  - [ ] game-data routes
- [ ] Implement centralized error handling.
- [ ] Implement retry and timeout behavior.
- [ ] Add response caching where appropriate.
- [ ] Support raw JSON responses and typed DTO responses.
- [ ] Validate authentication/session propagation in client requests.

### Exit criteria

- [ ] The client can reach D&D Beyond endpoints reliably in a mocked test environment.
- [ ] Failure cases produce clear, actionable errors.

## Phase 3: Domain model and mapping layer

- [ ] Introduce Java records / POJOs for core data models:
  - [ ] characters
  - [ ] campaigns
  - [ ] spells
  - [ ] monsters
  - [ ] items
  - [ ] feats
  - [ ] conditions
  - [ ] classes
  - [ ] races
  - [ ] backgrounds
  - [ ] class features
  - [ ] racial traits
  - [ ] game config metadata
- [ ] Add DTO mappers for unstable or oversized API responses.
- [ ] Normalize names, IDs, stat blocks, source metadata, and common enums.
- [ ] Define stable output shapes for MCP tool responses.

### Exit criteria

- [ ] All core API payloads map into stable Java models.
- [ ] Response formatting helpers are reusable across tools.

## Phase 4: Service layer

- [ ] Implement `AuthService` for login/session validation.
- [ ] Implement `CharacterService` for read/write character actions.
- [ ] Implement `CampaignService` for campaign membership and party data.
- [ ] Implement `ReferenceService` for compendium and lookup queries.
- [ ] Implement `PromptService` for prompt payload generation.
- [ ] Implement `ResourceService` for structured URI payloads.
- [ ] Keep orchestration logic out of the MCP entrypoints where possible.

### Exit criteria

- [ ] Each service has a clear responsibility and can be tested independently.

## Phase 5: Read-only reference tools

Implement the read-only reference surface first:

- [ ] `search_spells`
- [ ] `get_spell`
- [ ] `search_monsters`
- [ ] `get_monster`
- [ ] `search_items`
- [ ] `get_item`
- [ ] `search_feats`
- [ ] `get_condition`
- [ ] `search_classes`
- [ ] `search_races`
- [ ] `search_backgrounds`
- [ ] `search_class_features`
- [ ] `search_racial_traits`

### Work items

- [ ] Match existing tool names and argument semantics.
- [ ] Reproduce search/filter behavior, including client-side fallback where needed.
- [ ] Preserve sorting and truncation behavior where it matters for UX.
- [ ] Validate caching and paging against mocked responses.

### Exit criteria

- [ ] Reference tools return stable, readable outputs.
- [ ] Tool signatures are aligned with the legacy MCP contract.

## Phase 6: Character and campaign read tools

- [ ] `list_characters`
- [ ] `get_character`
- [ ] `get_definition`
- [ ] `list_campaigns`
- [ ] `get_campaign_characters`
- [ ] Character/campaign structured resources for:
  - [ ] character sheets
  - [ ] spell lists
  - [ ] inventories
  - [ ] campaigns
  - [ ] party rosters

### Work items

- [ ] Support lookup by ID and by name where the legacy server supported both.
- [ ] Keep resource URIs stable and discoverable.
- [ ] Ensure the resource payloads are useful without requiring extra post-processing by the LLM.

### Exit criteria

- [ ] Character and campaign reads work end-to-end.
- [ ] Resources are available for direct structured access.

## Phase 7: Character write tools

- [ ] HP and temporary HP updates
- [ ] spell slot updates
- [ ] death saves
- [ ] currency updates
- [ ] ability usage tracking
- [ ] rest flows
- [ ] spell casting
- [ ] class/species/background/feat choice resolution
- [ ] character creation and editing helpers

### Work items

- [ ] Recreate state-changing behavior only after read flows are stable.
- [ ] Add strict validation for required IDs and choice fields.
- [ ] Verify that mutation responses are surfaced clearly to the MCP client.
- [ ] Test destructive or stateful operations separately from read flows.

### Exit criteria

- [ ] Write tools behave predictably in mocked integration tests.
- [ ] Error messages are clear enough for LLM-driven recovery.

## Phase 8: Prompt catalog

- [ ] Recreate `character-summary`.
- [ ] Recreate `session-prep`.
- [ ] Recreate `encounter-builder`.
- [ ] Recreate `spell-advisor`.
- [ ] Recreate `level-up-guide`.
- [ ] Recreate `rules-lookup`.

### Work items

- [ ] Keep prompts concise and action-oriented.
- [ ] Make each prompt direct the model toward the right tool sequence.
- [ ] Avoid embedding implementation details in prompt text.
- [ ] Verify prompt parameter schemas and generated user-message payloads.

### Exit criteria

- [ ] The prompt catalog is discoverable through MCP.
- [ ] Each prompt matches the intent of the legacy server.

## Phase 9: Hardening and quality gates

- [ ] Add integration tests against mocked D&D Beyond responses.
- [ ] Add contract tests for MCP tool signatures and prompt payloads.
- [ ] Add resource URI and payload tests.
- [ ] Validate caching, retry, and timeout behavior.
- [ ] Verify authentication edge cases and expired-session handling.
- [ ] Add test coverage for representative reference, character, and campaign flows.
- [ ] Document all tools, prompts, and resources in the README.

### Exit criteria

- [ ] The server is stable under mocked integration tests.
- [ ] The public MCP contract is documented.

## Migration mapping to preserve

### Authentication

- [ ] `setup_auth`
- [ ] `check_auth`

### Character

- [ ] `get_character`
- [ ] `list_characters`
- [ ] `get_definition`
- [ ] `update_hp`
- [ ] `set_inspiration`
- [ ] `add_condition`
- [ ] `remove_condition`
- [ ] `update_spell_slots`
- [ ] `update_death_saves`
- [ ] `update_currency`
- [ ] `use_ability`
- [ ] `update_pact_magic`
- [ ] `long_rest`
- [ ] `short_rest`
- [ ] `cast_spell`
- [ ] `create_character`
- [ ] `delete_character`
- [ ] `add_class`
- [ ] `set_background`
- [ ] `set_background_choice`
- [ ] `set_class_feature_choice`
- [ ] `set_race_trait_choice`
- [ ] `set_feat_choice`
- [ ] `resolve_choices`
- [ ] `set_species`
- [ ] `set_ability_score`
- [ ] `update_character_name`
- [ ] `set_class_level`
- [ ] `set_ability_score_type`
- [ ] `set_starting_equipment_type`
- [ ] `add_inventory_items`
- [ ] `set_gold`
- [ ] `update_description`

### Campaign

- [ ] `list_campaigns`
- [ ] `get_campaign_characters`

### Reference

- [ ] `search_spells`
- [ ] `get_spell`
- [ ] `search_monsters`
- [ ] `get_monster`
- [ ] `search_items`
- [ ] `get_item`
- [ ] `search_feats`
- [ ] `get_condition`
- [ ] `search_classes`
- [ ] `search_races`
- [ ] `search_backgrounds`
- [ ] `search_class_features`
- [ ] `search_racial_traits`

## Definition of done

- [ ] The Java server exposes a comparable MCP tool set.
- [ ] The prompt catalog is recreated.
- [ ] The resource URIs are available for character/campaign data.
- [ ] Read flows are working and tested.
- [ ] Mutation flows are implemented where supported by the D&D Beyond APIs.
- [ ] The README documents the new MCP interface and setup steps.
- [ ] The project can be run as a Spring Boot MCP server from `dnd_rules_mcp_server`.
