package com.alphabetas.caller.command;

import com.alphabetas.caller.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

public class IAmHereCommand implements Command {

    private MessageService messageService;

    private String[] phrases = new String[] {"Я тут та готовий вас кликати ;)", "Хтось мене кликав?"};

    public IAmHereCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String phrase = phrases[new Random().nextInt(phrases.length)];
        messageService.sendMessage(chatId, phrase);
    }
}
