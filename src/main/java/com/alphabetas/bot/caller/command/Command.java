package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.*;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Command {

    protected static MessageService messageService;
    protected static CallerChatService chatService;
    protected static CallerUserService userService;
    protected static CallerNameService nameService;
    protected static ChatConfigService configService;

    protected String msgText;
    protected CallerChat chat;
    protected CallerUser user;
    protected Integer threadId;

    public Command(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        this.msgText = msgText;
        this.chat = chat;
        this.user = user;
        this.threadId = threadId;
    }

    public Command() {
    }

    public static void setService(MessageService messageService1, CallerChatService chatService1,
                                  CallerUserService userService1, CallerNameService nameService1,
                                  ChatConfigService configService1) {
        messageService = messageService1;
        chatService = chatService1;
        userService = userService1;
        nameService = nameService1;
        configService = configService1;
    }

    abstract public void execute(Update update);

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
