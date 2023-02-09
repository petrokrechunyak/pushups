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
//    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void checkForDeletion() {
        List<CallerChat> all = chatService.findAll();
        for(CallerChat chat: all) {
            for (CallerUser user: chat.getCallerUsers()) {
                ChatMember member = null;
                try {
                    member = messageService.getChatMember(chat.getId(), user.getUserId());
                } catch (Exception e) {
                    if(e.getMessage().contains("bot was kicked")) {
                        chatService.delete(chat);
                    }
                }
                System.out.println(member.toString());
                if(member instanceof ChatMemberLeft) {
                    userService.delete(user);
                    break;
                }
            }
        }
    }

}
