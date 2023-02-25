package com.alphabetas.bot.caller.command.group;

import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.enums.UserStates;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.GroupUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

@Slf4j
public class JoinCommand extends Command {

    public JoinCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public JoinCommand() {
    }

    private static final String[] createGroupMessages = new String[]{"Напишіть назву групового імені, до якого хочете приєднатися. Для відміни напишіть /cancel"};

    @Override
    public void execute(Update update) {

        log.info("Entered into JoinCommand");

        msgText = CommandUtils.trimMessage(msgText, getSpecialArgs());
        if (msgText.isBlank()) {
            sendMessageToLeaveGroup();
            return;
        }

        String resultText = GroupUtils.joinGroup(msgText, user, chat);
        messageService.sendMessage(chat.getId(), resultText, threadId);

    }

    private void sendMessageToLeaveGroup() {
        user.setUserState(UserStates.JOIN);
        userService.save(user);

        String sendMessageText = createGroupMessages[new Random().nextInt(createGroupMessages.length)];
        messageService.sendMessage(chat.getId(), sendMessageText, threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/join", "приєднатися", "приєднай", "ввійти"};
    }
}
