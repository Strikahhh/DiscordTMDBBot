package me.domen.discordBot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandExecutor {

    public void execute(MessageReceivedEvent event);
}
