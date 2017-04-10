package co.automod.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;

public class Main {
    //TODO: Make this a config.json file
    public static final String token = "";

    public static void main(String[] args) {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(token);
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
