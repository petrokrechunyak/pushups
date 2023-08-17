package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.Push;
import com.alphabetas.bot.caller.repo.PushRepo;
import com.alphabetas.bot.caller.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
public class StartCommand implements Command {

    private MessageService service;

    private PushRepo pushRepo;
    public StartCommand(MessageService service, PushRepo pushRepo) {
        this.service = service;
        this.pushRepo = pushRepo;
    }


    @Override
    public void execute(Update update) {
        User from = update.getMessage().getFrom();
        Push push = new Push(
                from.getId(),
                from.getUserName(),
                from.getFirstName(),
                0
        );

        pushRepo.save(push);
        service.sendMessage(from.getId(), "Все, вас зареєстровано!");
    }



}
