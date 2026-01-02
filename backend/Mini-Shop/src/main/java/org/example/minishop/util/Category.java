package org.example.minishop.util;

public enum Category {
    ELECTRONICS("Electronics"),
    BOOKS("Books"),
    FASHION("Fashion"),
    HOME_KITCHEN("Home & Kitchen"),
    BEAUTY("Beauty"),
    SPORTS("Sports"),
    TOYS("Toys");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

    public static Category fromDisplayName(String text) {
        for (Category c : Category.values()) {
            if (c.displayName.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + text);
    }
}
