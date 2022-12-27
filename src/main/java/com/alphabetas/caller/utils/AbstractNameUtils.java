package com.alphabetas.caller.utils;

import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractNameUtils {

    static CallerUserService userService;
    static CallerChatService chatService;
    static CallerNameService nameService;

    static MessageService messageService;

    public static void setServices(CallerUserService userService1, CallerChatService chatService1, CallerNameService nameService1,
                                   MessageService messageService1) {
        userService = userService1;
        chatService = chatService1;
        nameService = nameService1;
        messageService = messageService1;
    }

    public static String[] getNamesFromMessage(String message) {
        return Arrays.stream(message.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public static boolean isUserAdmin(Long userId, Long chatId) {
        List<ChatMember> admins = messageService.getAdminsByChatId(chatId);
        for (ChatMember admin: admins) {
            if(userId.equals(admin.getUser().getId()))
                return true;
        }
        return false;
    }

}
