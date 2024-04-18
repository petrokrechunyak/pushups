package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.repo.PushRepo;
import com.alphabetas.bot.caller.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class ClearCommand implements Command {

    private MessageService service;

    private PushRepo pushRepo;

    public ClearCommand(MessageService service, PushRepo pushRepo) {
        this.service = service;
        this.pushRepo = pushRepo;
    }

    @Override
    public void execute(Update update) {
//        pushRepo.deleteAllByUserId(update.getMessage().getFrom().getId());
    }
}
