package co.automod.bot.core;

import co.automod.bot.Config;
import co.automod.bot.ExitStatus;
import co.automod.bot.Main;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;

public class Shard {
    public void reboot(int shardId, int totShards) throws RateLimitedException, InterruptedException {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Config.discord_token);
        if (totShards > 1) {
            builder.useSharding(shardId, totShards);
        }
        JDA jda;
        try {
            jda = builder.buildBlocking();
            jda.setEventManager(new AnnotatedEventManager());
            //Register events
        } catch (LoginException e) {
            Main.exit(ExitStatus.INVALID_CONFIG);
        }
    }
}
