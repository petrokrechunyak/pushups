package com.alphabetas.caller.command;

import com.alphabetas.caller.command.Command;
import com.alphabetas.caller.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class IAmHereCommand implements Command {

    private MessageService messageService;

    public IAmHereCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        messageService.sendMessage(chatId, "Я тут та готовий вас кликати ;)");
    }
}
