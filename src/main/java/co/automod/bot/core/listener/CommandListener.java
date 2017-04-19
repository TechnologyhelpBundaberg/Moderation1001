package co.automod.bot.core.listener;

import co.automod.bot.Config;
import co.automod.bot.ExitStatus;
import co.automod.bot.Main;
import co.automod.bot.commands.CommandCategory;
import co.automod.bot.core.Settings;
import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.entities.*;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;


public class CommandListener {
    public final static HashMap<String, Command> commands;
    public final static HashMap<String, String> commandAliases;
//    private static final ConcurrentHashMap<Long, String> prefixes = new ConcurrentHashMap<>();

    static {
        commands = new HashMap<>();
        commandAliases = new HashMap<>();
        collectCommands();
    }

    public static String getPrefix(Guild guild) {
        if (guild == null) {
            return Config.default_command_prefix;
        }
        return Settings.getSetting(guild).prefix;
    }

    public boolean isCommand(String input, String prefix) {
        if (input == null || !input.startsWith(prefix)) {
            return false;
        }
        String[] split = input.split(" ", 2);
        if (split[0].length() <= prefix.length()) {
            return false;
        }
        String cmd = split[0].substring(prefix.length());
        return getCommand(cmd) != null;
    }

    /**
     * Retrieves an instance of command by commandname
     *
     * @param commandName name of the command (or alias)
     * @return Command || null if not found
     */
    private Command getCommand(String commandName) {
        if (commands.containsKey(commandName)) {
            return commands.get(commandName);
        }
        if (commandAliases.containsKey(commandName)) {
            return commands.get(commandAliases.get(commandName));
        }
        return null;
    }

    private static void collectCommands() {
        Reflections reflections = new Reflections("co.automod.bot.commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> s : classes) {
            try {
                if (Modifier.isAbstract(s.getModifiers())) {
                    continue;
                }
                Command command = s.getConstructor().newInstance();
                if (!commands.containsKey(command.getTrigger())) {
                    commands.put(command.getTrigger(), command);
                }
                for (String alias : command.getAliases()) {
                    commandAliases.put(alias, command.getTrigger());
                }
                String packageName = s.getPackage().getName();
                packageName = packageName.substring(packageName.lastIndexOf(".") + 1);
                command.setCategory(CommandCategory.findByPackage(packageName));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                Main.exit(ExitStatus.COMMAND_INITIALIZATION);
            }
        }
    }

    /**
     * Executes the command
     *
     * @param guild   guild
     * @param channel TextChannel
     * @param author  invoker/author
     * @param message the mesage
     * @return successfully executed?
     */
    public boolean execute(Guild guild, TextChannel channel, User author, Member member, Message message, String prefix) {
        String[] split = message.getRawContent().split(" ", 2);
        if (split[0].length() <= prefix.length()) {
            return false;
        }
        String cmd = split[0].substring(prefix.length());
        Command command = getCommand(cmd);
        if (command == null) {
            return false;
        }
        command.execute(guild, channel, author, member, message, split.length == 1 ? "" : split[1]);
        return true;
    }
}
