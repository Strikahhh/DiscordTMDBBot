package me.domen.discordBot;

import me.domen.discordBot.commands.CommandExecutor;
import me.domen.discordBot.commands.GalleryCommand;
import me.domen.discordBot.commands.HelpCommand;
import me.domen.discordBot.commands.SearchCommand;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


public class Bot extends ListenerAdapter {

    public static final Properties properties = loadProperties();
    private static final Map<String, CommandExecutor> botCommandsExecutor = Map.of(
            "!search", new SearchCommand(),
            "!gallery", new GalleryCommand(),
            "!help", new HelpCommand()
    );

    public static void main(String[] args) {
        try {
            JDABuilder builder = JDABuilder.createDefault(properties.getProperty("token"));
            builder.setActivity(Activity.watching("you."));
            builder.addEventListeners(new Bot());
            builder.build();
        }catch (final LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        final String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args[0].startsWith("!")) {
            final CommandExecutor executor = botCommandsExecutor.getOrDefault(args[0], null);
            if (executor != null) botCommandsExecutor.get(args[0]).execute(event);
            else {
                final MessageChannel messageChannel = event.getChannel();
                messageChannel.sendMessage("Error: No command found.").queue();
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final String generalChannel = properties.getProperty("generalChannel");
        final MessageChannel general = event.getJDA().getTextChannelById(generalChannel);
        if (general == null) return;
        general.sendMessage("I am awoken.\nType !help for more information").queue();
    }

    public static Properties loadProperties() {
        final Properties properties = new Properties();
        try(final InputStream is = Bot.class.getResourceAsStream("/config.properties")) {
            if (is != null) properties.load(is);
            else {
                System.out.println("Resource config.properties not found");
                System.exit(-1);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
