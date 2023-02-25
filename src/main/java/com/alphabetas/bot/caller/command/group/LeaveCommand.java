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
public class LeaveCommand extends Command {

    public LeaveCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public LeaveCommand() {
    }

    private static final String[] createGroupMessages = new String[]{"Напишіть назву групового імені, яке хочете покинути. Для відміни напишіть /cancel"};

    @Override
    public void execute(Update update) {

        log.info("Entered into LeaveCommand");

        msgText = CommandUtils.trimMessage(msgText, getSpecialArgs());
        if (msgText.isBlank()) {
            sendMessageToLeaveGroup();
            return;
        }

        String resultText = GroupUtils.leaveGroup(msgText, user, chat);
        messageService.sendMessage(chat.getId(), resultText, threadId);

    }

    private void sendMessageToLeaveGroup() {
        user.setUserState(UserStates.LEAVE);
        userService.save(user);

        String sendMessageText = createGroupMessages[new Random().nextInt(createGroupMessages.length)];
        messageService.sendMessage(chat.getId(), sendMessageText, threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/leave", "покинути", "вийти", "залишити"};
    }
}
