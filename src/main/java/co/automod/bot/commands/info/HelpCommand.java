package co.automod.bot.commands.info;

import co.automod.bot.commands.CommandCategory;
import co.automod.bot.core.listener.CommandListener;
import co.automod.bot.core.listener.command.Command;
import co.automod.bot.util.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class HelpCommand extends Command {
    private String fullHelpText = null;

    @Override
    public String getTrigger() {
        return "help";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"?", "helpme", "commands"};
    }

    @Override
    public String getDescription() {
        return "Command list";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "help", "full overview of all the commands",
                "help <command>", "Info about a specific command"
        };
    }

    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        if (fullHelpText == null) {
            createHelpCache();
        }
        invoker.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(new EmbedBuilder()
                .setAuthor("AutoMod Commands!", null, channel.getJDA().getSelfUser().getAvatarUrl())
                .setDescription(String.format(fullHelpText, CommandListener.getPrefix(guild)))
                .setFooter("AutoMod ready at your command!", null)
                .build()).queue());
        if (PermissionUtil.checkPermission(channel, guild.getSelfMember(), Permission.MESSAGE_ADD_REACTION)) {
            message.addReaction(Emoji.MAILBOX_WITH_MAIL).queue();
        }
    }

    private void createHelpCache() {
        StringBuilder sb = new StringBuilder();
        HashMap<CommandCategory, TreeSet<String>> cmds = new HashMap<>();
        for (Map.Entry<String, Command> entry : CommandListener.commands.entrySet()) {
            Command value = entry.getValue();
            if (!value.isListed()) continue;
            if (!cmds.containsKey(value.getCategory())) {
                cmds.put(value.getCategory(), new TreeSet<>());
            }
            cmds.get(value.getCategory()).add(value.getTrigger());
        }
        for (CommandCategory category : CommandCategory.values()) {
            if (!cmds.containsKey(category)) {
                continue;
            }
            TreeSet<String> set = cmds.get(category);
            if (set.isEmpty()) {
                continue;
            }
            sb.append(category.getEmote()).append(" **").append(category.getCategoryName()).append("**\n");
            for (String trigger : set) {
                sb.append(String.format("[%s%s](https://github.com/repulser/automod) - %s\n", "%1$s", trigger, CommandListener.commands.get(trigger).getDescription()));
            }
            sb.append("\n");
        }
        fullHelpText = sb.toString();
    }
}
