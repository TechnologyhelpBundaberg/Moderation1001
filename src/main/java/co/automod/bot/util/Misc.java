package co.automod.bot.util;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Misc {
    private static final Pattern mentionUserPattern = Pattern.compile("<@!?([0-9]{8,})>");
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

    /**
     * Checks if the string contains a mention for a role
     *
     * @param input string to check for mentions
     * @return found a mention
     */
    public static boolean isUserMention(String input) {
        return mentionUserPattern.matcher(input).find();
    }

    /**
     * Converts a user mention to an id
     *
     * @param mention the mention to filter
     * @return a stripped down version of the mention
     */
    public static String mentionToId(String mention) {
        String id = "";
        Matcher matcher = mentionUserPattern.matcher(mention);
        if (matcher.find()) {
            id = matcher.group(1);
        }
        return id;
    }

    /**
     * Attempts to find a user from mention, if that fails see {@link Misc#findUserIn(TextChannel, String)}
     *
     * @param channel    the channel context
     * @param searchText the search argument
     * @return User || null
     */
    public static User findUser(TextChannel channel, String searchText) {
        if (Misc.isUserMention(searchText)) {
            return channel.getJDA().getUserById(Misc.mentionToId(searchText));
        } else {
            Member member = Misc.findUserIn(channel, searchText);
            if (member != null) {
                return member.getUser();
            }
        }
        return null;
    }

    /**
     * Attempts to find a user in a channel, first look for account name then for nickname
     *
     * @param channel    the channel to look in
     * @param searchText the name to look for
     * @return Member | null
     */
    public static Member findUserIn(TextChannel channel, String searchText) {
        List<Member> users = channel.getGuild().getMembers();
        List<Member> potential = new ArrayList<>();
        int smallestDiffIndex = 0, smallestDiff = -1;
        for (Member u : users) {
            String nick = u.getEffectiveName();
            if (nick.equalsIgnoreCase(searchText)) {
                return u;
            }
            if (nick.toLowerCase().contains(searchText)) {
                potential.add(u);
                int d = Math.abs(nick.length() - searchText.length());
                if (d < smallestDiff || smallestDiff == -1) {
                    smallestDiff = d;
                    smallestDiffIndex = potential.size() - 1;
                }
            }
        }
        if (!potential.isEmpty()) {
            return potential.get(smallestDiffIndex);
        }
        return null;
    }
}
