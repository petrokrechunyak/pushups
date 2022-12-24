package com.alphabetas.caller.utils;

import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;

import java.util.Arrays;

public abstract class AbstractNameUtils {

    static CallerUserService userService;
    static CallerChatService chatService;
    static CallerNameService nameService;

    public static void setServices(CallerUserService userService1, CallerChatService chatService1, CallerNameService nameService1) {
        userService = userService1;
        chatService = chatService1;
        nameService = nameService1;
    }

    public static String[] getNamesFromMessage(String message) {
        return Arrays.stream(message.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

}
