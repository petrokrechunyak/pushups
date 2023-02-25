package com.alphabetas.bot.caller.command.group;

import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.GroupName;
import com.alphabetas.bot.caller.model.enums.UserStates;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.GroupUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

@Slf4j
public class AllCommand extends Command {

    public AllCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public AllCommand() {
    }

    @Override
    public void execute(Update update) {
        log.info("Entered into AllCommand");

        StringBuilder builder = new StringBuilder("Всі групові імена чату: <b>").append(CommandUtils.deleteBadSymbols(chat.getTitle())).append("</b>\n");
        for(GroupName groupName: chat.getGroupNames()) {
            builder.append(groupName.getName()).append("\n");
        }

        messageService.sendMessage(chat.getId(), builder.toString(), threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/all", "групові імена", "всі"};
    }

}
