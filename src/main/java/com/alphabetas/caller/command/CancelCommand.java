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
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@NoArgsConstructor
@Slf4j
public class CancelCommand extends Command{

    public CancelCommand(String msgText, CallerChat chat, CallerUser user) {
        super(msgText, chat, user);
    }

    @Override
    public void execute(Update update) {

        // delete message if starts with /
        if(msgText.startsWith("/")) {
            messageService.deleteMessage(chat.getId().toString(), update.getMessage().getMessageId());
        }

        if(msgText.startsWith("!") && update.getMessage().getReplyToMessage() != null) {
            if(AbstractNameUtils.isUserAdmin(user.getUserId(), chat.getId())) {
                CommandUtils.setUserToUpdate(update);
            } else {
                messageService.sendMessage(chat.getId(), "Тільки адміністратори можуть керувати іменами інших!");
                return;
            }
        }
        if(user.getUserState() != UserStates.OFF) {
            user.setUserState(UserStates.OFF);
            userService.save(user);
            messageService.sendMessage(chat.getId(), "Дію відхилено");
            return;
        }
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/cancel",
                "відміна", "відмінити"};
    }
}
