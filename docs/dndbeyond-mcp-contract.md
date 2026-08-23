# `dndbeyond-mcp` Contract Capture

## Purpose

This document captures the current MCP-facing contract of the TypeScript `dndbeyond-mcp` project so the Java/Spring reimplementation can preserve the same user-visible behavior where practical.

## Scope of the legacy MCP server

The legacy server exposes three MCP surfaces:

- **Tools** for auth, character operations, campaign access, and reference search/detail
- **Prompts** for guided workflows such as session prep and level-up help
- **Resources** for structured access to characters and campaigns

## Tool inventory

### Authentication

- `setup_auth` — browser-based login and session capture
- `check_auth` — verify that the current session is valid

### Character management

- `get_character`
- `list_characters`
- `get_definition`
- `update_hp`
- `set_inspiration`
- `add_condition`
- `remove_condition`
- `update_spell_slots`
- `update_death_saves`
- `update_currency`
- `use_ability`
- `update_pact_magic`
- `long_rest`
- `short_rest`
- `cast_spell`
- `create_character`
- `delete_character`
- `add_class`
- `set_background`
- `set_background_choice`
- `set_class_feature_choice`
- `set_race_trait_choice`
- `set_feat_choice`
- `resolve_choices`
- `set_species`
- `set_ability_score`
- `update_character_name`
- `set_class_level`
- `set_ability_score_type`
- `set_starting_equipment_type`
- `add_inventory_items`
- `set_gold`
- `update_description`

### Campaign access

- `list_campaigns`
- `get_campaign_characters`

### Reference search/detail

- `search_spells`
- `get_spell`
- `search_monsters`
- `get_monster`
- `search_items`
- `get_item`
- `search_feats`
- `get_condition`
- `search_classes`
- `search_races`
- `search_backgrounds`
- `search_class_features`
- `search_racial_traits`

## Prompt inventory

- `character-summary`
- `session-prep`
- `encounter-builder`
- `spell-advisor`
- `level-up-guide`
- `rules-lookup`

## Resource inventory

- `dndbeyond://characters`
- `dndbeyond://character/{id}`
- `dndbeyond://character/{id}/spells`
- `dndbeyond://character/{id}/inventory`
- `dndbeyond://campaigns`
- `dndbeyond://campaign/{id}/party`

## Key upstream endpoint groups used by the legacy server

### Character endpoints

Used for character read/write operations, builder flows, and state mutations.

### Campaign endpoints

Used for campaign listing and party rosters.

### Reference endpoints

Used for spells, monsters, items, feats, conditions, classes, races, backgrounds, class features, and racial traits.

### Game-data / config endpoints

Used by the reference tools to resolve metadata such as challenge ratings, monster types, size names, source books, and other lookup tables.

## Behavioral notes to preserve

- Search tools accept partial matches and often perform client-side filtering after loading reference data.
- Some tool flows depend on cached data and should remain fast on repeated lookups.
- Prompts are workflow starters, not full answers.
- Resources should provide stable, structured data for direct consumption by the LLM.
- Character mutation tools are stateful and should be treated carefully.

## Migration implication

The Java implementation should aim for:

- the same tool names
- the same prompt names
- the same resource URIs
- compatible parameter semantics where feasible
- compatible response structure where feasible
