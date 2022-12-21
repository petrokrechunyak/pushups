package com.alphabetas.caller.command.container;

import com.alphabetas.caller.command.*;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private final Map<String, Command> commands;
    private final MessageService messageService;

    // All Commands
    private Command noCommand, unknown, addName, iamhere, cancel, show, delete;


    public CommandContainer(MessageService messageService, CallerChatService chatService,
                            CallerUserService userService, CallerNameService nameService) {
        this.commands = new HashMap<>();
        this.messageService = messageService;

        //create commands
        unknown = new UnknownCommand();
        iamhere = new IAmHereCommand(messageService);
        noCommand = new NoCommand(userService, chatService, nameService, messageService);
        addName = new AddNameCommand(userService, chatService, nameService, messageService);
        cancel = new CancelCommand(userService, chatService, nameService, messageService);
        show = new ShowCommand(userService, chatService, nameService, messageService);
        delete = new DeleteCommand(userService, chatService, nameService, messageService);

        prepareCommands();

    }

    public Command retrieveCommand(String command){
            return commands.getOrDefault(command.split("[ @]")[0].toLowerCase(), unknown);
    }

    public Command retrieveText(String text){

        text = text.toLowerCase();
        String[] splitted = text.split(" ", 3);
        if(text.equals("кликун")) {
            return iamhere;
        }
        if(text.startsWith("кликун ")) {
            return commands.getOrDefault(splitted[1], noCommand);
        } else if(text.startsWith(".")) {
            return commands.getOrDefault(splitted[0], noCommand);
        }
        return noCommand;
    }

    public void prepareCommands() {
        // add command
        prepareCommand(AddNameCommand.specialArgs, addName);
        // cancel command
        prepareCommand(CancelCommand.specialArgs, cancel);
        // show command
        prepareCommand(ShowCommand.specialArgs, show);
        // delete command
        prepareCommand(DeleteCommand.specialArgs, delete);

    }

    public void prepareCommand(String[] arg, Command command) {
        arg = Arrays.stream(arg)
                .map(x -> x.replaceFirst("кликун ", ""))
                .toArray(String[]::new);

        for(String s: arg) {
            commands.put(s, command);
        }
    }
}
