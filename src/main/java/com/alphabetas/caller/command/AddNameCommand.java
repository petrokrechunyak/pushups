package com.alphabetas.caller.command;

import com.alphabetas.caller.utils.AddNameUtils;
import com.alphabetas.caller.utils.CommandUtils;
import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.model.enums.UserStates;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;

import java.util.Arrays;
import java.util.Random;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

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

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user = userService.getByUserIdAndCallerChat(chat, update);

        // remove start of message, leaving only arguments
        msgText = CommandUtils.trimMessage(msgText, specialArgs);
        if (msgText.isBlank()) {
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
