package co.automod.bot.commands.config;

import co.automod.bot.core.listener.command.Command;
import co.automod.bot.data.GuildBool;
import com.rethinkdb.gen.exc.ReqlNonExistenceError;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;

import static co.automod.bot.Main.conn;
import static co.automod.bot.Main.r;

public class AntiLinkCommand extends Command {
    @Override
    public String getTrigger() {
        return "antilink";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"al"};
    }

    @Override
    public String getDescription() {
        return "Toggle anti link setting.";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "antilink", "toggles Anti Link",
                "antilink on/off", "turns Anti Link on or off"
        };
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(guild, guild.getMember(invoker), Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        Boolean bool = true;
        boolean enabled;
        try {
            enabled = r.table("antilink").get(guild.getId()).getField("bool").run(conn);
        } catch (ReqlNonExistenceError ignored) {
            enabled = false;
        }
        if (args.equalsIgnoreCase("false") || args.equalsIgnoreCase("off")) {
            bool = false;
        } else if (enabled) {
            bool = false;
        }
        r.table("antilink").insert(new GuildBool(guild.getId(), bool)).optArg("conflict", "replace")
                .runNoReply(conn);
        if (bool) {
            channel.sendMessage("\u2705 **Anti Link has been enabled!**").queue();
        } else {
            channel.sendMessage("\u274C **Anti Link has been disabled**").queue();
        }
    }
}
