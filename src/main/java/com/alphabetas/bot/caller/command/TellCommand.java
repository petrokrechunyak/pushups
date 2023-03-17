package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Random;

@NoArgsConstructor
@Slf4j
public class TellCommand extends Command {

    public TellCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    @Override
    public void execute(Update update) {
        log.info("Entered into TellCommand");

        Long myId = 731921794L;
        if(!user.getUserId().equals(myId) || !chat.getId().equals(myId)) {
            return;
        }
        msgText = msgText.replace("/tell ", "");
        List<CallerChat> all = chatService.findAll();
        for(CallerChat current: all) {
            try {
                messageService.sendMessage(current.getId(), msgText, threadId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/tell"};
    }
}

