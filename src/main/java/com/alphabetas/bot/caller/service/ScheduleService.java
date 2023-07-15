package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.MessageCount;
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

    @Autowired
    private MessageCountService messageCountService;

    private MessageService messageService;

    private static final int SECOND = 1000;
    private static final int DAY = 86400 * SECOND;

    public ScheduleService(CallerBot bot) {
        messageService = new MessageServiceImpl(bot);
    }

    // every 0.5 hour
    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void checkForDeletion() {
        List<CallerChat> all = chatService.findAll();
        for(CallerChat chat: all) {
            int counter = 0;
            for (CallerUser user: chat.getCallerUsers()) {
                ChatMember member = null;
                int currentSize = user.getNames().size();
                try {
                    member = messageService.getChatMember(chat.getId(), user.getUserId());
                } catch (Exception e) {
                    if(e.getMessage().contains("bot was kicked")) {
                        chatService.delete(chat);
                        continue;
                    } else if(e.getMessage().contains("user not found")) {
                        userService.delete(user);
                        continue;
                    }else {
                        e.printStackTrace();
                    }
                }
                if(member instanceof ChatMemberLeft) {
                    userService.delete(user);
                    chat.getCallerUsers().remove(user);
                    counter += currentSize;
                    break;
                }
            }
            if(counter > 0) {
                messageService.sendMessage(chat.getId(), "Очищено імен: " + counter, (Integer) null);
            }
            try {
                chat.setMemberCount(messageService.getChatMemberCount(chat.getId()));
                chatService.save(chat);
            } catch (Exception e) {
                String msg = e.getMessage();
                if(msg.contains("chat not found") || msg.contains("chat was deleted")
                        || msg.contains("bot was kicked")
                        || msg.contains("group chat was upgraded to a supergroup chat")) {
                    chatService.delete(chat);
                    continue;
                } else {
                    e.printStackTrace();
                }
            }

            if(chat.getTitle() == null) {
                chatService.delete(chat);
            }
        }
    }

    // every 100 seconds
    @Scheduled(fixedDelay = 1000 * 100)
    public void checkForOldMessages() {
        List<MessageCount> all = messageCountService.getAll();
        long current = System.currentTimeMillis();
        current = current - (current % 100000);
        for (MessageCount messageCount: all) {
            if(current - DAY > messageCount.getStartTime()) {
                messageCountService.delete(messageCount);
            }
        }
    }

}
