package jrbj.training.cookbook.recipe.search_strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for all search strategies
 */
@AllArgsConstructor
public enum SearchStrategies {
    ALL("All"),
    ANY("Any"),
    NONE("None");

    @Getter
    private final String name;

    /**
     * Map of all enum values to enable convertion from name attribute to Enum value ignoring case
     */
    private static final Map<String, SearchStrategies> ENUM_MAP;

    static {
        Map<String, SearchStrategies> map = new HashMap<>();
        for (SearchStrategies instance : SearchStrategies.values()) {
            map.put(instance.getName().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Retrieve enum value by providing its name attribute.
     * Method is case-insensitive
     */
    public static SearchStrategies get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }



}
