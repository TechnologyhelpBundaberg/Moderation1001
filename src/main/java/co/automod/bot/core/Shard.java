package co.automod.bot.core;

import co.automod.bot.Config;
import co.automod.bot.ExitStatus;
import co.automod.bot.Main;
import co.automod.bot.core.listener.CommandListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import javax.security.auth.login.LoginException;

public class Shard {
    private final ShardContainer container;
    private final CommandListener commandListener;

    Shard(ShardContainer container) {
        this.container = container;
        commandListener = new CommandListener();
    }

    public void reboot(int shardId, int totShards) throws RateLimitedException, InterruptedException {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Config.discord_token);
        if (totShards > 1) {
            builder.useSharding(shardId, totShards);
        }
        JDA jda;
        try {
            jda = builder.buildBlocking();
            jda.setEventManager(new AnnotatedEventManager());
            jda.addEventListener(new ModLog());
            jda.addEventListener(new AntiLink());
            jda.addEventListener(this);
        } catch (LoginException e) {
            e.printStackTrace();
            Main.exit(ExitStatus.INVALID_CONFIG);
        }
    }

    @SubscribeEvent
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            return;
        }
        if (!commandListener.isCommand(e.getMessage().getContent())) {
            return;
        }
        commandListener.execute(e.getGuild(), e.getChannel(), e.getAuthor(), e.getMessage());
        //yey
    }
}
