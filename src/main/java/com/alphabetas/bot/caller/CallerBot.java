package com.alphabetas.bot.caller;

import com.alphabetas.bot.caller.command.CommandContainer;
import com.alphabetas.bot.caller.model.Push;
import com.alphabetas.bot.caller.repo.PushRepo;
import com.alphabetas.bot.caller.service.MessageService;
import com.alphabetas.bot.caller.service.impl.MessageServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class CallerBot extends TelegramLongPollingBot {
    private CommandContainer container;

    private MessageService messageService;

    private PushRepo pushRepo;

    public CallerBot(PushRepo pushRepo) throws Exception {
        this.pushRepo = pushRepo;
        this.messageService = new MessageServiceImpl(this);
        this.container = new CommandContainer(messageService, pushRepo);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.startsWith("/")) {
                container.retrieveCommand(text).execute(update);
            } else if (StringUtils.isNumeric(text)) {
                User user = update.getMessage().getFrom();
                Push push = pushRepo.getByUserId(user.getId());
                push.setAllPushUps(push.getAllPushUps() + Integer.parseInt(text));
                pushRepo.save(push);
                messageService.sendMessage(user.getId(), "Збережено!\nЗагальна кількість віджимань: " + push.getAllPushUps());
            }
        }
    }

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {

    }
}
