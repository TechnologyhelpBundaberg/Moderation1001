package co.automod.bot.core.filters;

public abstract class AbstractFilter {

    private int costs;
    private FilterType type;

    public int getCosts() {
        return costs;
    }

    public abstract FilterType getType();

    public abstract String getWarnText();

}
