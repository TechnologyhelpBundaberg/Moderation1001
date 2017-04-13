package co.automod.bot.core;

import co.automod.bot.Config;
import co.automod.bot.ExitStatus;
import co.automod.bot.Main;
import co.automod.bot.core.listener.CommandListener;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rethinkdb.gen.exc.ReqlNonExistenceError;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static co.automod.bot.Main.conn;
import static co.automod.bot.Main.r;

public class Shard {
    private final ShardContainer container;
    private final CommandListener commandListener;
    private final ExecutorService commandExecutor;
    private final int shardId;
    private final int totShards;
    private static JDA jda;

    Shard(ShardContainer container, int shardId, int totShards) {
        this.container = container;
        this.shardId = shardId;
        this.totShards = totShards;
        commandListener = new CommandListener();
        ThreadFactoryBuilder threadBuilder = new ThreadFactoryBuilder();
        threadBuilder.setNameFormat(String.format("shard-%02d-command-%%d", shardId));
        this.commandExecutor = Executors.newCachedThreadPool(threadBuilder.build());
    }

    public void reboot() throws RateLimitedException, InterruptedException {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Config.discord_token);
        if (totShards > 1) {
            builder.useSharding(shardId, totShards);
        }
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

    public JDA getJda() {
        return jda;
    }

    public static String getPrefix(Guild guild) {
        if (guild == null) {
            return Config.default_command_prefix;
        }
        try {
            return r.table("prefixes").get(guild.getId()).getField("prefix").run(conn);

        } catch (ReqlNonExistenceError ignored) {
            return Config.default_command_prefix;
        }
    }

    @SubscribeEvent
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            return;
        }
        String prefix = getPrefix(e.getGuild());
        if (!commandListener.isCommand(e.getMessage().getContent(), prefix)) {
            //Send the servers prefix
            if (e.getMessage().getRawContent().equalsIgnoreCase(e.getJDA().getSelfUser().getAsMention())) {
                e.getChannel().sendMessage("**My prefix here is `" + getPrefix(e.getGuild()) + "`**").queue();
            }
            return;
        }
        commandExecutor.submit(() -> {
            commandListener.execute(e.getGuild(), e.getChannel(), e.getAuthor(), e.getMessage(), prefix);
        });
    }
}
