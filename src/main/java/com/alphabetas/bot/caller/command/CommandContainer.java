package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.repo.PushRepo;
import com.alphabetas.bot.caller.service.MessageService;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private final Map<String, Command> commands;
    private final MessageService messageService;

    // All Commands
    Command start, stop, unknown, stats, say;

    public CommandContainer(MessageService messageService, PushRepo pushRepo) {
        this.commands = new HashMap<>();
        this.messageService = messageService;

        start = new StartCommand(messageService, pushRepo);
        stats = new StatsCommand(messageService, pushRepo);
        commands.put("/start", start);
        commands.put("/stop", stop);
        commands.put("/stats", stats);
        commands.put("/say", say);
    }

    public Command retrieveCommand(String command){
            return commands.getOrDefault(command.split("[ @]")[0].toLowerCase(), unknown);
    }

}
