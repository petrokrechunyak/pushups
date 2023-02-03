package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public abstract class Command {

    protected static MessageService messageService;
    protected static CallerChatService chatService;
    protected static CallerUserService userService;
    protected static CallerNameService nameService;

    protected String msgText;
    protected CallerChat chat;
    protected CallerUser user;

    public Command(String msgText, CallerChat chat, CallerUser user) {
        this.msgText = msgText;
        this.chat = chat;
        this.user = user;
    }

    public Command() {
    }

    abstract public void execute(Update update);

    public static void setService(MessageService messageService1, CallerChatService chatService1,
                    CallerUserService userService1, CallerNameService nameService1) {
        messageService = messageService1;
        chatService = chatService1;
        userService = userService1;
        nameService = nameService1;
    }

    public String[] getSpecialArgs() {
        return null;
    }

    @Override
    public String toString() {
        return "Command{" +
                ", msgText='" + msgText + '\'' +
                ", chat=" + chat +
                ", user=" + user +
                '}';
    }
}
