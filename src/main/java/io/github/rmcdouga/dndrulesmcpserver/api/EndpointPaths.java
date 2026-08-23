package io.github.rmcdouga.dndrulesmcpserver.api;

/**
 * Centralized endpoint path constants and small URI builders for D&D Beyond.
 *
 * <p>Keeps literal endpoint strings out of services and client code.</p>
 */
public final class EndpointPaths {

    private EndpointPaths() {
        // utility class
    }

    // --- Character endpoints ---
    public static final String CHARACTERS = "/api/characters";
    public static final String CHARACTER = "/api/characters/%d";
    public static final String CHARACTER_DEFINITIONS = "/api/characters/%d/definitions";

    // --- Campaign endpoints ---
    public static final String CAMPAIGNS = "/api/campaigns";
    public static final String CAMPAIGN_CHARACTERS = "/api/campaigns/%d/characters";

    // --- Reference endpoints ---
    public static final String SPELLS = "/api/spells";
    public static final String MONSTERS = "/api/monsters";
    public static final String ITEMS = "/api/items";
    public static final String FEATS = "/api/feats";
    public static final String CLASSES = "/api/classes";
    public static final String RACES = "/api/races";
    public static final String BACKGROUNDS = "/api/backgrounds";
    public static final String CLASS_FEATURES = "/api/class-features";
    public static final String RACIAL_TRAITS = "/api/racial-traits";

    // --- Game-data / config endpoints ---
    public static final String GAME_CONFIG = "/api/game-config";
    public static final String CONDITIONS = "/api/conditions";

    // --- Auth / session ---
    public static final String AUTH_CHECK = "/api/auth/status";

    // --- URI builders ---

    public static String characterPath(long characterId) {
        return String.format(CHARACTER, characterId);
    }

    public static String characterDefinitionsPath(long characterId) {
        return String.format(CHARACTER_DEFINITIONS, characterId);
    }

    public static String campaignCharactersPath(long campaignId) {
        return String.format(CAMPAIGN_CHARACTERS, campaignId);
    }
}
