package co.automod.bot.util;

import java.util.Arrays;
import java.util.HashSet;

public class Misc {
    private final static HashSet<String> fuzzyTrue = new HashSet<>(Arrays.asList("yea", "yep", "yes", "true", "ja", "y", "t", "1", "check"));
    private final static HashSet<String> fuzzyFalse = new HashSet<>(Arrays.asList("no", "false", "nope", "nein", "nee", "n", "f", "0", "off"));

    /**
     * whether a string can fuzzily considered true
     *
     * @param text the string
     * @return true if it can be considered true
     */
    public static boolean isFuzzyTrue(String text) {
        return text != null && fuzzyTrue.contains(text.toLowerCase());
    }

    /**
     * whether a string can fuzzily considered true
     *
     * @param text the string to check
     * @return true if it can be considered false
     */
    public static boolean isFuzzyFalse(String text) {
        return text != null && fuzzyFalse.contains(text.toLowerCase());
    }
}
