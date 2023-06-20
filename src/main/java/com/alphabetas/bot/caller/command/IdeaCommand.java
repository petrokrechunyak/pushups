package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.CallerBot;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
@Slf4j
public class IdeaCommand extends Command {

    @Override
    public void execute(Update update) {
        if (msgText.replace("/idea", "").isEmpty()) {
            messageService.sendMessage(chat.getId(), "Напишіть свою ідею в одному повідомленню з командою.", update.getMessage().getMessageId().longValue());
            return;
        }
        messageService.sendMessage(CallerBot.MY_ID, msgText, (Integer) null);
        messageService.sendMessage(chat.getId(), "Ваша ідея була відправлена розробнику. Дякую!", update.getMessage().getMessageId().longValue());
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/idea"};
    }
}
