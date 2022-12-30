package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.model.enums.UserStates;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.utils.AbstractNameUtils;
import com.alphabetas.caller.utils.CommandUtils;
import com.alphabetas.caller.utils.DeleteNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.List;
import java.util.Random;

@Slf4j
public class DeleteCommand implements Command {

    private CallerUserService userService;
    private CallerChatService chatService;
    private CallerNameService nameService;
    private MessageService messageService;

    public static final String[] specialArgs = new String[]{"/delete_names", "/delete_name", "/delete",
            "видалити імена", "видалити ім'я", "видалити",
            "видали імена", "видали ім'я", "видали",
    };

    private static final  String[] deleteNameMessages = new String[]{"Напишіть ім'я, яке хочете видалити\n" +
            "(Якщо нічого не хочете видаляти напишіть /cancel)",
            "Все, а тепер просто напишіть ім'я яке хочете видалити, але якщо не хочете нічого видаляти напишіть /cancel",
            "Хочете видалити ім'я? Просто напишіть його. Не хочете? - Напишіть /cancel"};

    public DeleteCommand(CallerUserService userService, CallerChatService chatService, CallerNameService nameService, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();

        log.info("Entered into DeleteCommand with text {}", msgText);
        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user;

        boolean admin = false;
        if(msgText.startsWith("!") && update.getMessage().getReplyToMessage() != null) {
            if(AbstractNameUtils.isUserAdmin(userId, chatId)) {
                CommandUtils.setUserToUpdate(update);
                admin = true;
            } else {
                messageService.sendMessage(chatId, "Тільки адміністратори можуть керувати іменами інших!");
                return;
            }
        }


        user = userService.getByUserIdAndCallerChat(chat, update);
        msgText = CommandUtils.trimMessage(msgText, specialArgs);
        System.out.println(msgText);
        if (msgText.isBlank()) {
            sendMessageToDeleteName(chatId, user);
            return;
        }

        if(admin) {
            messageService.sendMessage(chatId, "Параметри повинні бути в одному повідомленні адмін-командою!");
            return;
        }

        String savedText = DeleteNameUtils.deleteNames(msgText, user, chat);

        messageService.sendMessage(chatId, savedText);
    }


    public void sendMessageToDeleteName(Long chatId, CallerUser user) {
        user.setUserState(UserStates.DELETE);
        userService.save(user);
        String sendMessageText = deleteNameMessages[new Random().nextInt(3)];
        messageService.sendMessage(chatId, sendMessageText);
    }

}
