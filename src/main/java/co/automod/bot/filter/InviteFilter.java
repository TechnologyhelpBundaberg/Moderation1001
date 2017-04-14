package co.automod.bot.filter;

import co.automod.bot.core.filters.RegexFilter;

import java.util.regex.Pattern;

public class InviteFilter extends RegexFilter {

    @Override
    public Pattern getPattern() {
        return Pattern.compile("discord(?:(\\.(?:me|io|gg)|sites\\.com)\\/.{0,4}|app\\.com.{1,4}(?:invite|oauth2).{0,5}\\/)\\w+");
    }

    @Override
    public String getWarnText() {
        return "%s \u26D4 **Advertising is not allowed!**";
    }
}
