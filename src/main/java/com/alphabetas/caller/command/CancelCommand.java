package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.model.enums.UserStates;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class CancelCommand implements Command{

    private final CallerUserService userService;
    private final CallerChatService chatService;
    private final CallerNameService nameService;
    private final MessageService messageService;
    public final static String[] specialArgs = new String[]{"/cancel",
            "відміна", "відмінити"};

    public CancelCommand(CallerUserService userService, CallerChatService chatService, CallerNameService nameService, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        log.info("Entered into CancelCommand with text {}", msgText);
        Long chatId = update.getMessage().getChatId();

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user = userService.getByUserIdAndCallerChat(chat, update);

        if(user.getUserState() != UserStates.OFF) {
            user.setUserState(UserStates.OFF);
            userService.save(user);
            messageService.sendMessage(chatId, "Дію відхилено");
            return;
        }
    }
}
