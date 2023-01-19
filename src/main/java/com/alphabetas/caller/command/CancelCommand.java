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
        Long userId = update.getMessage().getFrom().getId();

        // delete message if starts with /
        if(msgText.startsWith("/")) {
            messageService.deleteMessage(chatId.toString(), update.getMessage().getMessageId());
        }

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user = userService.getByUserIdAndCallerChat(chat, update);

        if(msgText.startsWith("!") && update.getMessage().getReplyToMessage() != null) {
            if(AbstractNameUtils.isUserAdmin(userId, chatId)) {
                CommandUtils.setUserToUpdate(update);
            } else {
                messageService.sendMessage(chatId, "Тільки адміністратори можуть керувати іменами інших!");
                return;
            }
        }
        if(user.getUserState() != UserStates.OFF) {
            user.setUserState(UserStates.OFF);
            userService.save(user);
            messageService.sendMessage(chatId, "Дію відхилено");
            return;
        }
    }
}
