package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.model.enums.UserStates;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.utils.AddNameUtils;
import com.alphabetas.caller.utils.CommandUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

import static com.alphabetas.caller.utils.AbstractNameUtils.isUserAdmin;

@NoArgsConstructor
@Slf4j
public class AddNameCommand implements Command {

    private CallerChatService chatService;
    private CallerUserService userService;
    private CallerNameService nameService;
    private MessageService messageService;
    public final static String[] specialArgs = new String[]{"/add_name", "/add",
            "додай ім'я", "додай",
            "додати імена", "додати ім'я", "додати"};
    private String[] addNameMessages = new String[]{"Напишіть ім'я, яке хочете добавити\n" +
            "(Якщо нічого не хочете добавляти напишіть /cancel)",
            "Все, а тепер просто напишіть ім'я яке хочете добавити, але якщо не хочете нічого добавляти напишіть /cancel",
            "Хочете добавити ім'я? Просто напишіть його. Не хочете? - Напишіть /cancel"};

    private String[] badWords = new String[]{"кликун", "додати"};


    public AddNameCommand(CallerUserService userService, CallerChatService chatService, CallerNameService nameService, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        log.info("Entered into AddNameCommand with text {}", msgText);
        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user;

        boolean admin = false;

        if(msgText.startsWith("!") && update.getMessage().getReplyToMessage() != null) {
            if(isUserAdmin(userId, chatId)) {
                CommandUtils.setUserToUpdate(update);
                admin = true;
            } else {
                messageService.sendMessage(chatId, "Тільки адміністратори можуть керувати іменами інших!");
                return;
            }
        }


        user = userService.getByUserIdAndCallerChat(chat, update);

        // remove start of message, leaving only arguments
        msgText = CommandUtils.trimMessage(msgText, specialArgs);
        if (msgText.isBlank()) {
            if(admin) {
                messageService.sendMessage(chatId, "Параметри повинні бути в одному повідомленні з адмін-командою!");
                return;
            }
            sendMessageToAddName(chatId, user);
            return;
        }

        String savedText = AddNameUtils.saveNames(msgText, user, chat);

        messageService.sendMessage(chatId, savedText);


    }


    /**
     * Changes userState to ADD, what means that next message will add name to him
     * @param chatId chat, where message will be sent
     * @param user   user, whose UserState will be changed
     *
     */
    public void sendMessageToAddName(Long chatId, CallerUser user) {
        user.setUserState(UserStates.ADD);
        userService.save(user);
        String sendMessageText = addNameMessages[new Random().nextInt(3)];
        messageService.sendMessage(chatId, sendMessageText);
    }


}
