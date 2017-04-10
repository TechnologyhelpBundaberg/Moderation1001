package co.automod.bot;

import com.kaaz.configuration.ConfigurationBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        new ConfigurationBuilder(Config.class, new File("bot.cfg")).build();
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(Config.discord_token);
        JDA jda;
        try {
            jda = builder.buildBlocking();
            jda.setEventManager(new AnnotatedEventManager());
            //Register events
        } catch (LoginException | RateLimitedException | InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
