package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.enums.UserStates;
import com.alphabetas.bot.caller.utils.AddNameUtils;
import com.alphabetas.bot.caller.utils.CommandUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

import static com.alphabetas.bot.caller.utils.AbstractNameUtils.isUserAdmin;

@NoArgsConstructor
@Slf4j
public class AddNameCommand extends Command {

    private static final String[] addNameMessages = new String[]{"Напишіть ім'я, яке хочете додати\n" +
            "(Якщо нічого не хочете додавати напишіть /cancel)",
            "Все, а тепер просто напишіть ім'я яке хочете додати, але якщо не хочете нічого додавати напишіть /cancel",
            "Хочете додати ім'я? Просто напишіть його. Не хочете? - Напишіть /cancel"};

    private final String[] badWords = new String[]{"кликун", "додати"};

    public AddNameCommand(String msgText, CallerChat chat, CallerUser user) {
        super(msgText, chat, user);
    }

    @Override
    public void execute(Update update) {

        if (msgText.startsWith("/")) {
            messageService.deleteMessage(chat.getId().toString(), update.getMessage().getMessageId());
        }

        boolean admin = false;

        if (msgText.startsWith("!") && update.getMessage().getReplyToMessage() != null) {
            if (isUserAdmin(user.getUserId(), chat.getId())) {
                if (update.getMessage().getReplyToMessage().getFrom().getIsBot()) {
                    messageService.sendMessage(chat.getId(), "У ботів не може бути імен :/");
                    return;
                }
                CommandUtils.setUserToUpdate(update);
                admin = true;
            } else {
                messageService.sendMessage(chat.getId(), "Тільки адміністратори можуть керувати іменами інших!");
                return;
            }
        }

        user = userService.getByUpdate(update);

        // remove start of message, leaving only arguments
        msgText = CommandUtils.trimMessage(msgText, getSpecialArgs());
        if (msgText.isBlank()) {
            if (admin) {
                messageService.sendMessage(chat.getId(), "Параметри повинні бути в одному повідомленні з адмін-командою!");
                return;
            }
            sendMessageToAddName();
            return;
        }

        String savedText = AddNameUtils.saveNames(msgText, user, chat);

        messageService.sendMessage(chat.getId(), savedText);
    }


    /**
     * Changes userState to ADD, what means that next message will add name to him
     */
    public void sendMessageToAddName() {
        user.setUserState(UserStates.ADD);
        userService.save(user);
        String sendMessageText = addNameMessages[new Random().nextInt(3)];
        messageService.sendMessage(chat.getId(), sendMessageText);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/add_name", "/add",
                "додай імена", "додай ім'я", "додай",
                "додати імена", "додати ім'я", "додати"};
    }


}
