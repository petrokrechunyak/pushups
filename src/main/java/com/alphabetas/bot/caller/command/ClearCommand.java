package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;

public class ClearCommand extends Command{
    public ClearCommand(String msgText, CallerChat chat, CallerUser user) {
        super(msgText, chat, user);
    }

    public ClearCommand() {
    }

    @Override
    public void execute(Update update) {
        int counter = 0;
        for(CallerUser u: chat.getCallerUsers()) {
            ChatMember chatMember = messageService.getChatMember(chat.getId(), u.getUserId());
            if(chatMember instanceof ChatMemberLeft) {
                userService.delete(u);
                counter += u.getNames().size();
            }
        }
        messageService.sendMessage(chat.getId(), "Очищено імен: " + counter);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/clear", "очистити"};
    }
}
