package com.alphabetas.bot.caller.command.marriage;

import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@NoArgsConstructor
@Slf4j
public class FamilyCommand extends Command {


    public FamilyCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
        log.info("Entered into FamilyCommand");
    }

    @Override
    public void execute(Update update) {

    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/backup"};
    }


}
