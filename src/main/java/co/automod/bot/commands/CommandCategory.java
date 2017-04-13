package co.automod.bot.commands;

public enum CommandCategory {
    INFORMATIVE("info", "\u2139", "Information"),
    CONFIGURATION("config", "\u2699\ufe0f", "Configuration"),
    ADMINISTRATIVE("admin", "\uD83D\uDC6E", "Administration"),
    UNKNOWN("?", "\u2753", "Unknown"),;

    private final String packageName;
    private final String emote;
    private final String categoryName;

    CommandCategory(String packageName, String emote, String categoryName) {

        this.packageName = packageName;
        this.emote = emote;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getEmote() {
        return emote;
    }

    public String getPackageName() {
        return packageName;
    }

    public static CommandCategory findByPackage(String packageName) {
        if (packageName != null) {
            for (CommandCategory cc : values()) {
                if (packageName.equalsIgnoreCase(cc.packageName)) {
                    return cc;
                }
            }
        }
        return CommandCategory.UNKNOWN;
    }
}
