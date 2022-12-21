package com.alphabetas.caller.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class UnknownCommand implements Command{
    @Override
    public void execute(Update update) {
        log.info("Entered in UnknownCommand with command {}", update.getMessage().getText());
    }
}
