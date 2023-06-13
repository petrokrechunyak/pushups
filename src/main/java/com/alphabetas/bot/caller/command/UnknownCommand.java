package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@NoArgsConstructor
public class UnknownCommand extends Command {

    public UnknownCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    @Override
    public void execute(Update update) {
        log.info("Entered in UnknownCommand with command {}", update.getMessage().getText());
    }
}
