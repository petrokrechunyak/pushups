package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.Service;
import com.alphabetas.bot.caller.service.*;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Command extends Service {

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
