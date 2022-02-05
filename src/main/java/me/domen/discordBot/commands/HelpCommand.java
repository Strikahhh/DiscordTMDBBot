package me.domen.discordBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class HelpCommand implements CommandExecutor {

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.CYAN);
        eb.setTitle("List of available commands with examples");
        eb.addField("Search command", "!search <keywords> <page-optional>\nExample: !search iron_man 3\n" +
                "Searches for a movie with given keywords. Use page to find more results", false);
        eb.addField("Gallery command", "!gallery <movie-id>\n" +
                "Returns images for a specified movie\nIt is possible there are none", false);
        channel.sendMessageEmbeds(eb.build()).queue();
    }
}
