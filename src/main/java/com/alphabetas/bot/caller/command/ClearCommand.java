package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberBanned;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberRestricted;

@Slf4j
public class ClearCommand extends Command{

    public ClearCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public ClearCommand() {
    }

    @Override
    public void execute(Update update) {
        int counter = 0;
        log.info("searching users for deleting:");
        log.info(chat.getCallerUsers().toString());
        for(CallerUser u: chat.getCallerUsers()) {
            ChatMember chatMember = null;
            try {
                chatMember = messageService.getChatMember(chat.getId(), u.getUserId());
            } catch (Exception e) {
                userService.delete(u);
                counter += u.getNames().size();
                log.info("user to delete found:(Exception)" + u);
                continue;
            }
            log.info(chatMember.toString());
            if(chatMember instanceof ChatMemberLeft ||
                    chatMember instanceof ChatMemberBanned) {
                userService.delete(u);
                counter += u.getNames().size();
                log.info("user to delete found: " + u);
            } else if(chatMember instanceof ChatMemberRestricted) {
                ChatMemberRestricted restricted = (ChatMemberRestricted) chatMember;
                if(!restricted.getIsMember()) {
                    userService.delete(u);
                    counter += u.getNames().size();
                }
             }
        }
        messageService.sendMessage(chat.getId(), "Очищено імен: " + counter, threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/clear", "очистити"};
    }
}
