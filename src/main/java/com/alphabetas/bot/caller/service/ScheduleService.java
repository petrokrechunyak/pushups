package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.enums.UserStates;
import com.alphabetas.bot.caller.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;

import java.util.List;

@Component
public class ScheduleService {

    @Autowired
    private CallerChatService chatService;

    @Autowired
    private CallerUserService userService;

    private MessageService messageService;

    public ScheduleService(CallerBot bot) {
        messageService = new MessageServiceImpl(bot);
    }

    // every 1 hour
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void checkForDeletion() {
        List<CallerChat> all = chatService.findAll();
        for(CallerChat chat: all) {
            int counter = 0;
            for (CallerUser user: chat.getCallerUsers()) {
                ChatMember member = null;
                try {
                    member = messageService.getChatMember(chat.getId(), user.getUserId());
                } catch (Exception e) {
                    if(e.getMessage().contains("bot was kicked")) {
                        chatService.delete(chat);
                        continue;
                    } else {
                        e.printStackTrace();
                    }
                }
                if(member instanceof ChatMemberLeft) {
                    userService.delete(user);
                    chat.getCallerUsers().remove(user);
                    counter += user.getNames().size();
                    break;
                }
            }
            if(counter > 0) {
                messageService.sendMessage(chat.getId(), "Очищено імен: " + counter);
            }
            try {
                chat.setMemberCount(messageService.getChatMemberCount(chat.getId()));
                chatService.save(chat);
            } catch (Exception e) {
                if(e.getMessage().contains("chat not found")) {
                    chatService.delete(chat);
                    continue;
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

}
