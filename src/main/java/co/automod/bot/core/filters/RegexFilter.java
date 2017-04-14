package co.automod.bot.core.filters;

import java.util.regex.Pattern;

public abstract class RegexFilter extends AbstractFilter {

    public abstract Pattern getPattern();

    public FilterType getType() {
        return FilterType.REGEX;
    }
}
