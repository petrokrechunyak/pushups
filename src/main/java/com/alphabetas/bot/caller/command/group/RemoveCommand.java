package com.alphabetas.bot.caller.command.group;

import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.enums.UserStates;
import com.alphabetas.bot.caller.utils.AbstractNameUtils;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.GroupUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

@Slf4j
public class RemoveCommand extends Command {

    public RemoveCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public RemoveCommand() {
    }

    private static final String[] createGroupMessages = new String[]{"Напишіть назву групового імені, яке хочете створити. Для відміни напишіть /cancel"};

    @Override
    public void execute(Update update) {
        log.info("Entered into RemoveCommand");
        if (!AbstractNameUtils.isUserAdmin(user.getUserId(), chat.getId())) {
            messageService.sendMessage(chat.getId(), "Групові імена можуть видаляти лише адміністратори!", threadId);
            return;
        }

        msgText = CommandUtils.trimMessage(msgText, getSpecialArgs());
        if (msgText.isBlank()) {
            sendMessageToCreateGroup();
            return;
        }

        String resultText = GroupUtils.removeGroup(msgText, user, chat);
        messageService.sendMessage(chat.getId(), resultText, threadId);
    }

    public void sendMessageToCreateGroup() {
        user.setUserState(UserStates.DELETE_GROUP);
        userService.save(user);

        String sendMessageText = createGroupMessages[new Random().nextInt(createGroupMessages.length)];
        messageService.sendMessage(chat.getId(), sendMessageText, threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/remove", "стерти"};
    }
}
