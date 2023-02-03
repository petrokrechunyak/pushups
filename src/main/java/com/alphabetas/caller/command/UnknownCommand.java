package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class UnknownCommand extends Command {

    public UnknownCommand(String msgText, CallerChat chat, CallerUser user) {
        super(msgText, chat, user);
    }

    @Override
    public void execute(Update update) {
        log.info("Entered in UnknownCommand with command {}", update.getMessage().getText());
    }
}
