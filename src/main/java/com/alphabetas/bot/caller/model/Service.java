package com.alphabetas.bot.caller.model;

import com.alphabetas.bot.caller.command.marriage.service.MarriageService;
import com.alphabetas.bot.caller.command.premium.service.PremiumChatService;
import com.alphabetas.bot.caller.service.*;

public abstract class Service {

    protected static MessageService messageService;
    protected static CallerChatService chatService;
    protected static CallerUserService userService;
    protected static CallerNameService nameService;
    protected static ChatConfigService configService;
    protected static GroupNameService groupNameService;
    protected static MessageCountService messageCountService;
    protected static MarriageService marriageService;
    protected static PremiumChatService premiumChatService;

    public static void setService(MessageService messageService1, CallerChatService chatService1,
                                  CallerUserService userService1, CallerNameService nameService1,
                                  ChatConfigService configService1, GroupNameService groupNameService1,
                                  MessageCountService messageCountService1, MarriageService marriageService1,
                                  PremiumChatService premiumChatService1) {
        messageService = messageService1;
        chatService = chatService1;
        userService = userService1;
        nameService = nameService1;
        configService = configService1;
        groupNameService = groupNameService1;
        messageCountService = messageCountService1;
        marriageService = marriageService1;
        premiumChatService = premiumChatService1;
    }

}
