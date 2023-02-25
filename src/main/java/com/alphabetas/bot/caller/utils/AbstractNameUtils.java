package com.alphabetas.bot.caller.utils;

import com.alphabetas.bot.caller.model.Service;
import com.alphabetas.bot.caller.service.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractNameUtils extends Service {

    public static String[] getNamesFromMessage(String message) {
        return Arrays.stream(message.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public static boolean isUserAdmin(Long userId, Long chatId) {
        List<ChatMember> admins = messageService.getAdminsByChatId(chatId);
        for (ChatMember admin : admins) {
            if (userId.equals(admin.getUser().getId()))
                return true;
        }
        return false;
    }

}
