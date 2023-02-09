package com.alphabetas.bot.caller.command.container;

import com.alphabetas.bot.caller.command.*;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerNameService;
import com.alphabetas.bot.caller.service.CallerUserService;
import com.alphabetas.bot.caller.service.MessageService;
import com.alphabetas.bot.caller.utils.CommandUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    private final Map<String, Class<?>> classMap;
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

    public Command classToCommand(Class<?> clazz, CallerChat chat, CallerUser user, String text) {
        try {
            Constructor<?> constructor = clazz.getConstructor(String.class, CallerChat.class, CallerUser.class);
            return (Command) constructor.newInstance(text, chat, user);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Command retrieveCommand(String command, Update update) {
        CallerChat chat = chatService.getByUpdate(update);
        CallerUser user = userService.getByUpdate(update);
        String msgText = update.getMessage().getText();
        Class<?> clazz = classMap.getOrDefault(command.split("[ @]")[0].toLowerCase(), UnknownCommand.class);
        return classToCommand(clazz, chat, user, msgText);
    }

    public Command retrieveText(String text, Update update) {
        String startText = text;
        CallerChat chat = chatService.getByUpdate(update);
        CallerUser user = userService.getByUpdate(update);
        if (text.equalsIgnoreCase("кликун")) {
            return classToCommand(IAmHereCommand.class, chat, user, startText);
        }

        if (!CommandUtils.isCommand(text)) {
            return classToCommand(NoCommand.class, chat, user, startText);
        }
        text = text.toLowerCase().replaceFirst("^[.|!]|кликун?", "");
        text = StringUtils.replaceIgnoreCase(text, "кликун", "");
        String[] splitted = ArrayUtils.removeAllOccurrences(text.split(" ", 3), "");

        Class<?> clazz = classMap.getOrDefault(splitted[0], NoCommand.class);
        return classToCommand(clazz, chat, user, startText);
    }

    public void prepareCommands() {
        // add command
        prepareCommand(new AddNameCommand());
        // cancel command
        prepareCommand(new CancelCommand());
        // show command
        prepareCommand(new ShowCommand());
        // delete command
        prepareCommand(new DeleteCommand());
        // config command
        prepareCommand(new ConfigCommand());
        // clear command
        prepareCommand(new ClearCommand());
    }

    public void prepareCommand(Command command) {
        String[] arg = command.getSpecialArgs();
        arg = Arrays.stream(arg)
                .map(x -> x.replaceFirst("кликун ", ""))
                .toArray(String[]::new);

        for (String s : arg) {
            classMap.put(s, command.getClass());
        }
    }
}
