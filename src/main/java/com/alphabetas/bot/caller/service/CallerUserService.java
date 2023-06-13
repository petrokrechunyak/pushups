package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.impl.CallerUserServiceImpl;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallerUserService extends AbstractService<CallerUser> {

    CallerUser getByUpdate(Update update);

    CallerUser getByUserIdAndCallerChat(CallerChat callerChat, Update update);

    CallerUser getByUserIdAndCallerChat(Long userId, CallerChat callerChat);

    void removeByCallerChat(CallerChat chat);

    void removeByUserIdAndCallerChat(Long userId, CallerChat chat);

    CallerUserService setBot(CallerBot bot);

}
