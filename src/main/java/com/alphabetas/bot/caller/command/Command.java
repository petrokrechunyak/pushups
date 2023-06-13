package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.Service;
import com.alphabetas.bot.caller.utils.CommandUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public abstract class Command extends Service {

    protected String msgText;
    protected CallerChat chat;
    protected CallerUser user;
    protected Message repliedMessage;
    protected CallerUser repliedUser;
    protected Integer threadId;
    protected Update update;
    public static final Long TEST_CHAT_ID = -1001907509347L;

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

    public void createInstance(Update update) {

        CallerChat chat = chatService.getByUpdate(update);
        CallerUser user = userService.getByUpdate(update);
        user.setMentionedUser(CommandUtils.makeLink(user.getUserId(), user.getFirstname()));
        Message repliedMessage = update.getMessage().getReplyToMessage();
        String msgText = update.getMessage().getText();
        Integer threadId = update.getMessage().getIsTopicMessage() != null ? update.getMessage().getMessageThreadId() : null;

        this.msgText = msgText;
        this.chat = chat;
        this.user = user;
        this.repliedMessage = repliedMessage;
        this.threadId = threadId;
        this.update = update;

        if (repliedMessage != null) {
            this.repliedUser = userService.getByUserIdAndCallerChat(repliedMessage.getFrom().getId(), chat);
            this.repliedUser.setMentionedUser(CommandUtils.makeLink(repliedUser.getUserId(), repliedUser.getFirstname()));
        }
    }
}
