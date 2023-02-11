package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.enums.UserStates;
import com.alphabetas.bot.caller.utils.AbstractNameUtils;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.DeleteNameUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

@NoArgsConstructor
@Slf4j
public class DeleteCommand extends Command {

    private static final String[] deleteNameMessages = new String[]{"Напишіть ім'я, яке хочете видалити\n" +
            "(Якщо нічого не хочете видаляти напишіть /cancel)",
            "Все, а тепер просто напишіть ім'я яке хочете видалити, але якщо не хочете нічого видаляти напишіть /cancel",
            "Хочете видалити ім'я? Просто напишіть його. Не хочете? - Напишіть /cancel"};

    public DeleteCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    @Override
    public void execute(Update update) {
        // delete message if starts with /
        if (msgText.startsWith("/")) {
            messageService.deleteMessage(chat.getId().toString(), update.getMessage().getMessageId());
        }

        boolean admin = false;
        if (msgText.startsWith("!") && update.getMessage().getReplyToMessage() != null) {
            if (AbstractNameUtils.isUserAdmin(user.getUserId(), chat.getId())) {
                CommandUtils.setUserToUpdate(update);
                admin = true;
            } else {
                messageService.sendMessage(chat.getId(), "Тільки адміністратори можуть керувати іменами інших!", threadId);
                return;
            }
        }


        user = userService.getByUserIdAndCallerChat(chat, update);
        msgText = CommandUtils.trimMessage(msgText, getSpecialArgs());
        if (msgText.isBlank()) {
            if (admin) {
                messageService.sendMessage(chat.getId(), "Параметри повинні бути в одному повідомленні з адмін-командою!", threadId);
                return;
            }
            sendMessageToDeleteName();
            return;
        }


        String savedText = DeleteNameUtils.deleteNames(msgText, user, chat);

        messageService.sendMessage(chat.getId(), savedText, threadId);
    }


    public void sendMessageToDeleteName() {
        user.setUserState(UserStates.DELETE);
        userService.save(user);
        String sendMessageText = deleteNameMessages[new Random().nextInt(3)];
        messageService.sendMessage(chat.getId(), sendMessageText, threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/delete_names", "/delete_name", "/delete",
                "видалити імена", "видалити ім'я", "видалити",
                "видали імена", "видали ім'я", "видали",};
    }

}
