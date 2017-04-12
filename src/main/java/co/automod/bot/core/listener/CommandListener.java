package co.automod.bot.core.listener;

import co.automod.bot.Config;
import co.automod.bot.ExitStatus;
import co.automod.bot.Main;
import co.automod.bot.core.listener.command.Command;
import co.automod.bot.core.listener.command.CommandCategory;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

public class CommandListener {
    private final static HashMap<String, Command> commands;
    private final static HashMap<String, String> commandAliases;

    static {
        commands = new HashMap<>();
        commandAliases = new HashMap<>();
        collectCommands();
    }

    public boolean isCommand(String input) {
        if (input == null || !input.startsWith(Config.command_prefix)) {
            return false;
        }
        String[] split = input.split(" ", 2);
        if (split[0].length() <= Config.command_prefix.length()) {
            return false;
        }
        String cmd = split[0].substring(Config.command_prefix.length());
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
    public boolean execute(Guild guild, TextChannel channel, User author, Message message) {
        String[] split = message.getContent().split(" ", 2);
        if (split[0].length() <= Config.command_prefix.length()) {
            return false;
        }
        String cmd = split[0].substring(Config.command_prefix.length());
        Command command = getCommand(cmd);
        if (command == null) {
            return false;
        }
        command.execute(guild, channel, author, message, split.length == 1 ? "" : split[1]);
        return true;
    }
}
