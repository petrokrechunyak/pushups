package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerName;
import com.alphabetas.bot.caller.model.CallerUser;

import java.util.Set;

public interface CallerNameService extends AbstractService<CallerName> {

    CallerName getByCallerChatAndName(CallerChat chat, String name);

    Set<CallerName> getAllByCallerChat(CallerChat chat);

    void deleteAllByCallerUser(CallerUser user);

    Set<CallerName> getAllByCallerUser(CallerUser user);


}
