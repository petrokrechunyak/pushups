package com.alphabetas.bot.caller.command.container;

import com.alphabetas.bot.caller.command.*;
import com.alphabetas.bot.caller.command.group.*;
import com.alphabetas.bot.caller.command.marriage.AllMarriagesCommand;
import com.alphabetas.bot.caller.command.marriage.DivorceCommand;
import com.alphabetas.bot.caller.command.marriage.MarriageCommand;
import com.alphabetas.bot.caller.command.premium.command.PremiumCommand;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerNameService;
import com.alphabetas.bot.caller.service.CallerUserService;
import com.alphabetas.bot.caller.service.MessageService;
import com.alphabetas.bot.caller.utils.CommandUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private final Map<String, Command> classMap;
    private final MessageService messageService;
    private final CallerUserService userService;
    private final CallerChatService chatService;


    public CommandContainer(MessageService messageService, CallerChatService chatService,
                            CallerUserService userService, CallerNameService nameService) {
        this.messageService = messageService;
        this.userService = userService;
        this.chatService = chatService;

        classMap = new HashMap<>();

        prepareCommands();
    }

    public Command classToCommand(Command command, Update update) {

        command.createInstance(update);
        return command;
    }

    public Command retrieveCommand(String command, Update update) {
        Command command1 = classMap.getOrDefault(command.split("[ @]")[0].toLowerCase(), new UnknownCommand());
        return classToCommand(command1, update);
    }

    public Command retrieveText(String text, Update update) {
        if (text.equalsIgnoreCase("кликун")) {
            return classToCommand(new IAmHereCommand(), update);
        }

        if (!CommandUtils.isCommand(text) && false) {
            return classToCommand(new NoCommand(), update);
        }
        text = text.toLowerCase().replaceFirst("^[.|!]|кликун?", "");
        text = StringUtils.replaceIgnoreCase(text, "кликун", "");
        String[] splitted = ArrayUtils.removeAllOccurrences(text.split(" ", 3), "");

        if(splitted.length == 0) {
            return new NoCommand();
        }
        Command command = classMap.getOrDefault(splitted[0], new NoCommand());
        return classToCommand(command, update);
    }

    public void prepareCommands() {
        prepareCommand(new AddNameCommand());
        prepareCommand(new CancelCommand());
        prepareCommand(new ShowCommand());
        prepareCommand(new DeleteCommand());
        prepareCommand(new ConfigCommand());
        prepareCommand(new ClearCommand());
        prepareCommand(new CreateCommand());
        prepareCommand(new JoinCommand());
        prepareCommand(new LeaveCommand());
        prepareCommand(new RemoveCommand());
        prepareCommand(new AllCommand());
        prepareCommand(new TopCommand());
        prepareCommand(new TipCommand());
        prepareCommand(new TellCommand());
        prepareCommand(new BackupCommand());
        prepareCommand(new MarriageCommand());
        prepareCommand(new AllMarriagesCommand());
        prepareCommand(new DivorceCommand());
        prepareCommand(new StartCommand());
        prepareCommand(new IdeaCommand());
        prepareCommand(new HelpCommand());
//        prepareCommand(new PremiumCommand());

    }

    public void prepareCommand(Command command) {
        String[] arg = command.getSpecialArgs();
        arg = Arrays.stream(arg)
                .map(x -> x.replaceFirst("кликун ", ""))
                .toArray(String[]::new);

        for (String s : arg) {
            classMap.put(s, command);
        }
    }
}
